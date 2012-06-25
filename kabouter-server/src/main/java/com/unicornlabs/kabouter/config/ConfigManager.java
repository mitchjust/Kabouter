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
package com.unicornlabs.kabouter.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ConfigManager.java
 * @author Mitch
 *
 * Description:
 * Holds the main properties for the application
 */

public class ConfigManager {

    private static final Logger LOGGER = Logger.getLogger(ConfigManager.class.getName());
    
    static{
        LOGGER.setLevel(Level.ALL);
    }
    
    private Properties myProperties;
    
    /**
     * Load the properties file
     * @param properties the stream to the properties file
     * @throws IOException 
     */
    public ConfigManager(InputStream properties) throws IOException {
        myProperties = new Properties();
        myProperties.load(properties);
    }
    
    /**
     * Get a value from the properties file
     * @param key the corresponding key
     * @return the value
     */
    public String getProperty(String key) {
        return myProperties.getProperty(key);
    }

}
