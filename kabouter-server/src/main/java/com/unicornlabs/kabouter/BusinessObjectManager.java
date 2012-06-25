// <editor-fold defaultstate="collapsed" desc="License">
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
// </editor-fold>

package com.unicornlabs.kabouter;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * BusinessObjectManager.java
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description:
 * Holds references to the main business objects
 */

public class BusinessObjectManager {

    private static final Logger LOGGER = Logger.getLogger(BusinessObjectManager.class.getName());
    
    private static BusinessObjectManager theInstance;
    
    static{
        LOGGER.setLevel(Level.ALL);
    }
    
    /**
     * Static method to get reference to singleton BOM
     * @return the BOM
     */
    public synchronized static BusinessObjectManager getInstance() {
        //If the BOM hasn't be initialized, create a new one
        if(theInstance == null) {
            theInstance = new BusinessObjectManager();
        }
        
        return theInstance;
    }
    
    /**
     * Register an object in the BOM
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
    private HashMap<String,Object> myBusinessObjects;
    
    /**
     * Constructor
     */
    public BusinessObjectManager() {
        myBusinessObjects = new HashMap();
    }
    
    

}
