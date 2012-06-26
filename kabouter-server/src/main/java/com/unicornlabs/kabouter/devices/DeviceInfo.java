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
 * DeviceInfo.java
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description:
 * Device Manager representation of Device Objects
 */

public class DeviceInfo {

    private static final Logger LOGGER = Logger.getLogger(DeviceInfo.class.getName());
    
    static{
        LOGGER.setLevel(Level.ALL);
    }
    
    public Device theDevice;
    public boolean isConnected;

}
