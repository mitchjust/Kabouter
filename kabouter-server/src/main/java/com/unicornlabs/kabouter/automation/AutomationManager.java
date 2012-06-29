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
package com.unicornlabs.kabouter.automation;

import com.unicornlabs.kabouter.BusinessObjectManager;
import com.unicornlabs.kabouter.clients.ClientManager;
import com.unicornlabs.kabouter.config.ConfigManager;
import com.unicornlabs.kabouter.devices.DeviceManager;
import com.unicornlabs.kabouter.gui.GuiManager;
import com.unicornlabs.kabouter.historian.Historian;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AutomationManager.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: TODO Add Class Description
 */
public class AutomationManager {

    private static final Logger LOGGER = Logger.getLogger(AutomationManager.class.getName());

    static {
        LOGGER.setLevel(Level.ALL);
    }
    
}
