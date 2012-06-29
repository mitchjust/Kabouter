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

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * KabouterConstants.java
 *
 * @author Mitch
 *
 * Description: Contains Constant variables
 */
public class KabouterConstants {

    private static final Logger LOGGER = Logger.getLogger(KabouterConstants.class.getName());

    static {
        LOGGER.setLevel(Level.ALL);
    }
    //App specific constants
    public static final String APP_NAME = "Kabouter Server";
    public static final String APP_VERSION = "V1.0 SNAPSHOT";
    public static final String FRAME_TITLE = APP_NAME + " " + APP_VERSION;
    //Jar File constants
    public static final String CONFIG_PATH = "/com/unicornlabs/kabouter/config/";
    public static final String LOGGING_PROPERTIES = "logging.properties";
    public static final String KABOUTER_SERVER_PROPERTIES = "kabouterserver.properties";
    //Local File constants
    public static final String APP_DIRECTORY = System.getProperty("user.home")
            + File.separatorChar
            + ".KabouterServer"
            + File.separatorChar;
    public static String MODULE_MANAGER_SAVE_FILE = "modules.dat";
    //Boolean constants
    public static int BOOLEAN_TRUE = -1;
    public static int BOOLEAN_FALSE = 0;
}
