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

import com.unicornlabs.kabouter.BusinessObjectManager;
import com.unicornlabs.kabouter.config.ConfigManager;
import com.unicornlabs.kabouter.devices.events.DeviceEvent;
import com.unicornlabs.kabouter.devices.events.DeviceEventListener;
import com.unicornlabs.kabouter.historian.Historian;
import com.unicornlabs.kabouter.historian.data_objects.Device;
import com.unicornlabs.kabouter.net.TCPChannelServer;
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
    private HashMap<String, DeviceInfo> myDeviceInfos;
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
        myDeviceInfos = new HashMap<String, DeviceInfo>();
        myDeviceEventListeners = new ArrayList<DeviceEventListener>();
        generateDeviceInfos();
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
    private void generateDeviceInfos() {
        ArrayList<Device> devices = theHistorian.getDevices();

        for (Device d : devices) {
            LOGGER.log(Level.INFO, "Loading Config For Device: {0}", d.getId());
            DeviceInfo di = new DeviceInfo(d);
            myDeviceInfos.put(d.getId(), di);
        }
    }

    /**
     * Gets a reference to the main list of Device Infos
     *
     * @return the list
     */
    public DeviceInfo[] getDeviceInfos() {
        Set<String> keySet = myDeviceInfos.keySet();
        DeviceInfo[] deviceInfoList = new DeviceInfo[keySet.size()];

        Iterator<String> iterator = keySet.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            String key = iterator.next();
            deviceInfoList[i++] = myDeviceInfos.get(key);
        }

        return deviceInfoList;
    }

    /**
     * Gets the device info for a device id
     *
     * @param deviceId the device id
     * @return the device info
     */
    public DeviceInfo getDeviceInfo(String deviceId) {
        DeviceInfo get = myDeviceInfos.get(deviceId);
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
    
    public void fireDeviceEvent(DeviceEvent e) {
        for(DeviceEventListener del : myDeviceEventListeners) {
            del.handleDeviceEvent(e);
        }
    }

    /**
     * Updates IO States on remote device
     * @param di 
     */
    public void updateDeviceIOState(DeviceInfo di) {
        
    }
    
    public DeviceInfo insertNewDevice(Device newDevice) {
        DeviceInfo newDeviceInfo = new DeviceInfo(newDevice);
        myDeviceInfos.put(newDevice.getId(), newDeviceInfo);
        theHistorian.saveDevice(newDevice);
        
        DeviceEvent e = new DeviceEvent(this, DeviceEvent.NEW_DEVICE_EVENT, newDeviceInfo);
        fireDeviceEvent(e);
        
        return newDeviceInfo;
    }

}
