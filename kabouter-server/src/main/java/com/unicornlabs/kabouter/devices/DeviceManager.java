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
import com.unicornlabs.kabouter.devices.events.DeviceEventListener;
import com.unicornlabs.kabouter.historian.Historian;
import com.unicornlabs.kabouter.historian.data_objects.Device;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DeviceManager.java
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description:
 * Manages Devices connected to the system
 */

public class DeviceManager {

    private static final Logger LOGGER = Logger.getLogger(DeviceManager.class.getName());
    
    static{
        LOGGER.setLevel(Level.ALL);
    }
    
    private Historian theHistorian;
    private ArrayList<DeviceInfo> myDeviceInfos;
    private ArrayList<DeviceEventListener> myDeviceEventListeners;
    
    /**
     * Obtains Historian reference and generates DeviceInfo objects
     */
    public DeviceManager() {
        theHistorian = (Historian) BusinessObjectManager.getBusinessObject(Historian.class.getName());
        myDeviceInfos = new ArrayList<DeviceInfo>();
        myDeviceEventListeners = new ArrayList<DeviceEventListener>();
        generateDeviceInfos();
    }
    
    /**
     * Get the saved devices from the historian and generate DeviceInfo objects
     */
    private void generateDeviceInfos() {
        ArrayList<Device> devices = theHistorian.getDevices();
        
        for(Device d : devices) {
            LOGGER.log(Level.INFO, "Loading Config For Device: {0}", d.getId());
            DeviceInfo di = new DeviceInfo();
            di.theDevice = d;
            di.isConnected = false;
            myDeviceInfos.add(di);
        }
    }
    
    /**
     * Performs an Update on all Devices in the device manager
     */
    private void updateAllDeviceEntries() {
        for(DeviceInfo di : myDeviceInfos) {
            Device d = di.theDevice;
            theHistorian.updateDevice(d);
        }
    }

    /**
     * Attaches a new DeviceEventListener
     * @param newListener the new listener
     */
    public void addDeviceEventListener(DeviceEventListener newListener) {
        myDeviceEventListeners.add(newListener);
    }
    
    

}
