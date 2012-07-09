// <editor-fold defaultstate="collapsed" desc="License">
/*
 * Copyright 2012 Mitchell Just <mitch.just@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// </editor-fold>
package com.unicornlabs.kabouter.devices;

import com.unicornlabs.kabouter.devices.messaging.ServerDeviceMessage;
import com.unicornlabs.kabouter.BusinessObjectManager;
import com.unicornlabs.kabouter.config.ConfigManager;
import com.unicornlabs.kabouter.devices.events.DeviceEvent;
import com.unicornlabs.kabouter.devices.events.DeviceEventListener;
import com.unicornlabs.kabouter.historian.Historian;
import com.unicornlabs.kabouter.historian.data_objects.Device;
import com.unicornlabs.kabouter.historian.data_objects.Devicetemplate;
import com.unicornlabs.kabouter.net.TCPChannelServer;
import com.unicornlabs.kabouter.util.JSONUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DeviceManager.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: Manages Devices connected to the system
 */
public class DeviceManager {

    private static final Logger LOGGER = Logger.getLogger(DeviceManager.class.getName());

    static {
        LOGGER.setLevel(Level.ALL);
    }
    /**
     * Reference to the historian
     */
    private Historian theHistorian;
    
    private ConfigManager theConfigManager;
    /**
     * Map of devices to device ids
     */
    private HashMap<String, DeviceStatus> myDeviceStatuses;
    /**
     * List of event listeners
     */
    private ArrayList<DeviceEventListener> myDeviceEventListeners;

    private TCPChannelServer myTCPChannelServer;
    
    private int myPort;
    
    /**
     * Obtains Historian reference and generates DeviceInfo objects
     */
    public DeviceManager() {
        theHistorian = (Historian) BusinessObjectManager.getBusinessObject(Historian.class.getName());
        theConfigManager = (ConfigManager) BusinessObjectManager.getBusinessObject(ConfigManager.class.getName());
        this.myPort = Integer.parseInt(theConfigManager.getProperty(DeviceManager.class.getName(), "TCP_LISTENING_PORT"));
        myDeviceStatuses = new HashMap<String, DeviceStatus>();
        myDeviceEventListeners = new ArrayList<DeviceEventListener>();
        generateDeviceStatusMap();
    }
    
    /**
     * Starts the TCP Listening server
     */
    public void startServer() {
        myTCPChannelServer = new TCPChannelServer(myPort, new KabouterDevicePipelineFactory());
        myTCPChannelServer.run();
    }

    /**
     * Get the saved devices from the historian and generate DeviceInfo objects
     */
    private void generateDeviceStatusMap() {
        ArrayList<Device> devices = theHistorian.getDevices();

        for (Device d : devices) {
            LOGGER.log(Level.INFO, "Loading Config For Device: {0}", d.getId());
            DeviceStatus di = new DeviceStatus(d);
            myDeviceStatuses.put(d.getId(), di);
        }
    }

    /**
     * Gets a reference to the main list of Device Infos
     *
     * @return the list
     */
    public DeviceStatus[] getDeviceStatuses() {
        Set<String> keySet = myDeviceStatuses.keySet();
        DeviceStatus[] deviceInfoList = new DeviceStatus[keySet.size()];

        Iterator<String> iterator = keySet.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            String key = iterator.next();
            deviceInfoList[i++] = myDeviceStatuses.get(key);
        }

        return deviceInfoList;
    }

    /**
     * Gets the device info for a device id
     *
     * @param deviceId the device id
     * @return the device info
     */
    public DeviceStatus getDeviceStatus(String deviceId) {
        DeviceStatus get = myDeviceStatuses.get(deviceId);
        return get;
    }

    /**
     * Attaches a new DeviceEventListener
     *
     * @param newListener the new listener
     */
    public void addDeviceEventListener(DeviceEventListener newListener) {
        myDeviceEventListeners.add(newListener);
    }
    
    /**
     * Fires the event handling method of each of the listeners
     * @param e 
     */
    public void fireDeviceEvent(DeviceEvent e) {
        for(DeviceEventListener del : myDeviceEventListeners) {
            del.handleDeviceEvent(e);
        }
    }

    /**
     * Inserts a new device to the database and returns the new deviceinfo
     * @param newDevice the device details
     * @return the deviceinfo object
     */
    public DeviceStatus insertNewDevice(String deviceId, String deviceType, String ipAddress) {
        Device newDevice = getTemplatedDevice(deviceType);
        newDevice.setId(deviceId);
        newDevice.setDisplayname(deviceId);
        newDevice.setIpaddress(ipAddress);
        
        DeviceStatus newDeviceStatus = new DeviceStatus(newDevice);
        
        LOGGER.log(Level.INFO, "Added new DeviceInfo: \n{0}", newDeviceStatus);
        
        myDeviceStatuses.put(newDevice.getId(), newDeviceStatus);
        theHistorian.saveDevice(newDevice);
        
        DeviceEvent e = new DeviceEvent(this, DeviceEvent.NEW_DEVICE_EVENT, newDeviceStatus);
        fireDeviceEvent(e);
        
        return newDeviceStatus;
        
    }
    
    /**
     * Constructs a new device from a templated device
     * @param deviceType the template name
     * @return the new device
     */
    public Device getTemplatedDevice(String deviceType) {
        Device newDevice = new Device();
        Devicetemplate theDevicetemplate = theHistorian.getDevicetemplate(deviceType);
        
        if(theDevicetemplate == null) {
            LOGGER.log(Level.SEVERE, "Unknown Device Type: {0}", deviceType);
        }
        
        newDevice.setIodirections(theDevicetemplate.getIodirections());
        newDevice.setIonames(theDevicetemplate.getIonames());
        newDevice.setIotypes(theDevicetemplate.getIotypes());
        newDevice.setNumio(theDevicetemplate.getNumio());
        newDevice.setType(deviceType);
        
        return newDevice;
    }

    public void updateDeviceIOState(DeviceStatus di) {
        ServerDeviceMessage newMessage = new ServerDeviceMessage();
        newMessage.messageType = ServerDeviceMessage.IO_STATE_CHANGE;
        newMessage.data = JSONUtils.ToJSON(di.ioStates);
        di.tcpChannel.write(newMessage);
    }

    /**
     * Returns a list of the device display names
     * @return the list
     */
    public ArrayList<String> getDeviceDisplayNames() {
        ArrayList<String> deviceDisplayNames = new ArrayList<String>();
        DeviceStatus[] deviceInfos = getDeviceStatuses();
        
        for(DeviceStatus di : deviceInfos) {
            deviceDisplayNames.add(di.theDevice.getDisplayname());
        }
        
        return deviceDisplayNames;
    }

}
