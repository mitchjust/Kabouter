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
package com.unicornlabs.kabouter.historian;

import com.unicornlabs.kabouter.historian.data_objects.Device;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Settings;
import org.hibernate.impl.SessionFactoryImpl;

/**
 * Historian.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: Manages connections to PostgreSQL database
 */
public class Historian {

    private static final Logger LOGGER = Logger.getLogger(Historian.class.getName());

    static {
        LOGGER.setLevel(Level.ALL);
    }
    
    //static session factory
    private static SessionFactory SESSIONFACTORY;
    
    //syncronization lock
    private static final Object HISTORIANLOCK = new Object();
    
    /**
     * Get the settings for the session factory
     * @return the settings
     */
    public static Settings GetSettings() {
        if(SESSIONFACTORY == null) {
            synchronized(HISTORIANLOCK) {
                SESSIONFACTORY = HibernateUtil.getSessionFactory();
            }
        }
        
        return ((SessionFactoryImpl)SESSIONFACTORY).getSettings();
    }
    
    /**
     * Create a session factory if necessary
     */
    public Historian() {
        if(SESSIONFACTORY == null) {
            synchronized(HISTORIANLOCK) {
                SESSIONFACTORY = HibernateUtil.getSessionFactory();
            }
        }
    }

    /**
     * Close down the session factory
     */
    public void shutDown() {
        if (SESSIONFACTORY != null) {
            SESSIONFACTORY.close();
        }
    }

    /**
     * Gets a device with the specified id
     * @param deviceId the device id
     * @return the device
     */
    public Device getDevice(String deviceId) {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Device as device device.id ='" + deviceId + "'").list();
        session.getTransaction().commit();
        session.close();
        if (result.isEmpty()) {
            return null;
        }

        if (result.size() != 1) {
            throw new org.hibernate.NonUniqueResultException(result.size());
        }

        return (Device) result.get(0);
    }
    
    /**
     * Get all devices
     * @return arraylist of all devices
     */
    public ArrayList<Device> getDevices() {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Device as device").list();
        session.getTransaction().commit();
        session.close();
        if (result.isEmpty()) {
            return null;
        }

        return (ArrayList)result;
    }

    /**
     * Save a new device
     * @param theDevice the device to be saved
     */
    public void saveDevice(Device theDevice) {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        session.save(theDevice);
        session.getTransaction().commit();
        session.close();
    }
    
    /**
     * Updates a device
     * @param theDevice the device to be updated
     */
    public void updateDevice(Device theDevice) {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        session.update(theDevice);
        session.getTransaction().commit();
        session.close();
    }
}
