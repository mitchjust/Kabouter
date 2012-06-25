// <editor-fold defaultstate="collapsed" desc="License">
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
