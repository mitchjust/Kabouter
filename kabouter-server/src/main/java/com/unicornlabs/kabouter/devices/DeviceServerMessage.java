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

import com.unicornlabs.kabouter.historian.data_objects.Device;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DeviceServerMessage.java
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description:
 * TODO Add Class Description
 */

public class DeviceServerMessage {

    private static final Logger LOGGER = Logger.getLogger(DeviceServerMessage.class.getName());
    
    public static final String DEVICE_CONFIG = "DEVICE_CONFIG";
    public static final String POWER_LOG = "POWER_LOG";
    public static final String IO_STATE_CHANGE = "IO_STATE_CHANGE";
    
    static{
        LOGGER.setLevel(Level.ALL);
    }
    
    public String messageType;
    public Device device;
    public String data;

}