// <editor-fold defaultstate="collapsed" desc="License">
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
// </editor-fold>

package com.unicornlabs.kabouter.config;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * KabouterConstants.java
 * @author Mitch
 *
 * Description:
 * Contains Constant variables
 */

public class KabouterConstants {

    private static final Logger LOGGER = Logger.getLogger(KabouterConstants.class.getName());
    
    static{
        LOGGER.setLevel(Level.ALL);
    }
    
    //App specific constants
    public static final String APP_NAME = "Kabouter Server";
    public static final String APP_VERSION = "v0.1";
    public static final String FRAME_TITLE = APP_NAME + " " + APP_VERSION;
    
    //Jar File constants
    public static final String CONFIG_PATH = "/com/unicornlabs/kabouter/config/";
    public static final String LOGGING_PROPERTIES = "logging.properties";
    public static final String KABOUTER_SERVER_PROPERTIES = "kabouterserver.properties";
    
    //Local File constants
    public static final String APP_DIRECTORY = System.getProperty("user.home") + 
            File.separatorChar + 
            ".KabouterServer" + 
            File.separatorChar;
    public static String MODULE_MANAGER_SAVE_FILE = "modules.dat";
    
    

}
