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

import com.unicornlabs.kabouter.config.KabouterConstants;
import com.unicornlabs.kabouter.historian.data_objects.Device;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DeviceInfo.java
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description:
 * Device Manager representation of Device Objects
 */

public class DeviceInfo implements Serializable{

    private static final Logger LOGGER = Logger.getLogger(DeviceInfo.class.getName());
    
    static{
        LOGGER.setLevel(Level.ALL);
    }
    
    /**
     * Construct a DeviceInfo object
     * @param theDevice 
     */
    public DeviceInfo(Device theDevice) {
        //Create array
        ioStates = new Integer[theDevice.getNumio()];
        //Set connected state to false initially
        isConnected = false;
        
        this.theDevice = theDevice;
        
        //Set inital io states to false
        for(int i=0;i<theDevice.getNumio();i++) {
            ioStates[i] = KabouterConstants.BOOLEAN_FALSE;
        }
    }
    
    public Device theDevice;
    public boolean isConnected;
    public Integer[] ioStates;
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("----Device Info----").append("\n");
        sb.append(theDevice.getId()).append("\n");
        sb.append(theDevice.getDisplayname()).append("\n");
        sb.append(theDevice.getIpaddress()).append("\n");
        sb.append(theDevice.getType()).append("\n");
        sb.append("Connected: ").append(isConnected).append("\n");
        sb.append("Power Logging: ").append(theDevice.getHaspowerlogging()).append("\n");
        
        for(int i=0;i<theDevice.getNumio();i++) {
            sb.append("IO#").append(i).append(" {").append(theDevice.getIonames()
                    .get(i)).append("}: ").append(ioStates[i]).append("\n");
        }
        
        sb.append("-------------------").append("\n");
        
        return sb.toString();
    }

}
