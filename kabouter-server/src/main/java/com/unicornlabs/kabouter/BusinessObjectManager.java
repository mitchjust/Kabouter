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
package com.unicornlabs.kabouter;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * BusinessObjectManager.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: Holds references to the main business objects
 */
public class BusinessObjectManager {

    private static final Logger LOGGER = Logger.getLogger(BusinessObjectManager.class.getName());
    private static BusinessObjectManager theInstance;

    static {
        LOGGER.setLevel(Level.ALL);
    }

    /**
     * Static method to get reference to singleton BOM
     *
     * @return the BOM
     */
    public synchronized static BusinessObjectManager getInstance() {
        //If the BOM hasn't be initialized, create a new one
        if (theInstance == null) {
            theInstance = new BusinessObjectManager();
        }

        return theInstance;
    }

    /**
     * Register an object in the BOM
     *
     * @param objectName the name to register under
     * @param theObject the object reference
     */
    public static void registerBusinessObject(String objectName, Object theObject) {
        LOGGER.log(Level.INFO, "Registering Object: {0}", objectName);
        BusinessObjectManager theBOM = BusinessObjectManager.getInstance();
        theBOM.myBusinessObjects.put(objectName, theObject);
    }

    /**
     * Obtain a reference to the named object
     *
     * @param objectName the name of the object
     * @return the object reference
     */
    public static Object getBusinessObject(String objectName) {
        BusinessObjectManager theBOM = BusinessObjectManager.getInstance();
        return theBOM.myBusinessObjects.get(objectName);
    }
    /**
     * non-static map of objects
     */
    private HashMap<String, Object> myBusinessObjects;

    /**
     * Constructor
     */
    public BusinessObjectManager() {
        myBusinessObjects = new HashMap();
    }
}
