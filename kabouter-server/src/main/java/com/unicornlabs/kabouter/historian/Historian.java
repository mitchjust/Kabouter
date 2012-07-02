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
import com.unicornlabs.kabouter.historian.data_objects.Powerlog;
import com.unicornlabs.kabouter.historian.data_objects.PowerlogId;
import java.util.ArrayList;
import java.util.Date;
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
    //TODO Probably not necessary, HibernateUtil is thread-safe
    private static final Object HISTORIANLOCK = new Object();

    /**
     * Get the settings for the session factory
     *
     * @return the settings
     */
    public static Settings GetSettings() {
        if (SESSIONFACTORY == null) {
            synchronized (HISTORIANLOCK) {
                SESSIONFACTORY = HibernateUtil.getSessionFactory();
            }
        }

        return ((SessionFactoryImpl) SESSIONFACTORY).getSettings();
    }

    /**
     * Create a session factory if necessary
     */
    public Historian() {
        if (SESSIONFACTORY == null) {
            synchronized (HISTORIANLOCK) {
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
     *
     * @param deviceId the device id
     * @return the device
     */
    public Device getDevice(String deviceId) {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Device as device where device.id ='" + deviceId + "'").list();
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
     *
     * @return arraylist of all devices
     */
    public ArrayList<Device> getDevices() {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Device as device").list();
        session.getTransaction().commit();
        session.close();

        return (ArrayList) result;
    }

    /**
     * Save a new device
     *
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
     *
     * @param theDevice the device to be updated
     */
    public void updateDevice(Device theDevice) {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        session.update(theDevice);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Deletes a device
     *
     * @param theDevice the device to be deleted
     */
    public void deleteDevice(Device theDevice) {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        session.delete(theDevice);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Returns the power logs for a device id
     *
     * @param deviceId the device id
     * @return ArrayList of logs
     */
    public ArrayList<Powerlog> getPowerlogs(String deviceId) {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Powerlog as powerlog where powerlog.id.deviceid = '" + deviceId + "' order by powerlog.id.logtime").list();
        session.getTransaction().commit();
        session.close();

        return (ArrayList) result;
    }

    /**
     * Returns the power logs for a device id between two dates
     *
     * @param deviceId the device id
     * @param from the first date
     * @param to the last date
     * @return ArrayList of logs
     */
    public ArrayList<Powerlog> getPowerlogs(String deviceId, Date from, Date to) {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Powerlog as powerlog where powerlog.id.deviceid = '" + deviceId + "' "
                + "and powerlog.id.logtime between '" + from + "' and '" + to + "' order by powerlog.id.logtime").list();
        session.getTransaction().commit();
        session.close();

        return (ArrayList) result;
    }

    /**
     * Returns data sampled at regular intervals between two dates
     *
     * @param deviceId the device id
     * @param from the first date
     * @param to the last date
     * @param maxNumRecords the maximum number of data points to return
     * @return the logs
     */
    public ArrayList<Powerlog> getPowerlogs(String deviceId, Date from, Date to, int maxNumRecords) {
        ArrayList<Powerlog> preparedData = new ArrayList<Powerlog>();

        ArrayList<Powerlog> rawData = getPowerlogs(deviceId, from, to);

        /**
         * For no logs case, add a zero log at the start and finish
         */
        if (rawData.isEmpty()) {
            Powerlog startLog = new Powerlog(new PowerlogId(from, deviceId), 0d);
            Powerlog endLog = new Powerlog(new PowerlogId(to, deviceId), 0d);

            preparedData.add(startLog);
            preparedData.add(endLog);

            return preparedData;
        }

        /**
         * Adjust is the number or records is less than the requested count
         */
        if (rawData.size() < maxNumRecords) {
            maxNumRecords = rawData.size();
        }

        //Get the sampling interval
        long dateDifference = to.getTime() - from.getTime();
        long dateInterval = dateDifference / maxNumRecords;

        //Set the first sample to the first date
        long sampleDate = rawData.get(0).getId().getLogtime().getTime();

        //Start from the first log
        int logIndex = 0;

        //For each data point requested
        for (int i = 0; i < maxNumRecords; i++) {
            //The first sample date is half of the interval before the sample date
            long sampleFrom = sampleDate - dateInterval / 2;
            Date sampleFromDate = new Date(sampleFrom);
            //The last sample date is half of the interval after the sample date
            long sampleTo = sampleDate + dateInterval / 2;
            Date sampleToDate = new Date(sampleTo);

            double samplePower = getAveragePower(rawData, sampleFromDate, sampleToDate);

            Powerlog newLog = new Powerlog(new PowerlogId(new Date(sampleDate), deviceId), samplePower);
            preparedData.add(newLog);


            //Increment sample date
            sampleDate += dateInterval;
        }

        return preparedData;
    }

    /**
     * Gets the average power between two dates in a list of power logs
     *
     * @param logs the list of logs
     * @param from the start date
     * @param to the end date
     * @return the average power
     */
    public double getAveragePower(ArrayList<Powerlog> logs, Date from, Date to) {
        double avgPower = 0;
        int count = 0;

        //For each log
        for (Powerlog log : logs) {
            //If the date is or is after the start date
            if (log.getId().getLogtime().compareTo(from) >= 0) {
                //Break if we have gone past the end date
                if (log.getId().getLogtime().compareTo(to) > 0) {
                    break;
                }
                //Sum the power
                avgPower += log.getPower();
                count++;
            }
        }

        //Divide by count
        return avgPower / count;
    }

    /**
     * Returns all power logs from all devices between a date range
     *
     * @param from the start date
     * @param to the end date
     * @return the list of power logs
     */
    public ArrayList<Powerlog> getPowerlogs(Date from, Date to) {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Powerlog as powerlog where powerlog.id.logtime between '" + from + "' and '" + to + "'").list();
        session.getTransaction().commit();
        session.close();

        return (ArrayList) result;
    }

    /**
     * Saves a new power log
     *
     * @param thePowerlog the log to save
     */
    public void savePowerlog(Powerlog thePowerlog) {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        session.save(thePowerlog);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Saves a list of power logs
     *
     * @param thePowerlog the log to save
     */
    public void savePowerlogs(ArrayList<Powerlog> thePowerlogs) {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        for (Powerlog log : thePowerlogs) {
            session.save(log);
        }
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Updates a power log
     *
     * @param thePowerlog the log to update
     */
    public void updatePowerlog(Powerlog thePowerlog) {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        session.update(thePowerlog);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Deletes a specified power log
     *
     * @param thePowerlog the power log to delete
     */
    public void deletePowerlog(Powerlog thePowerlog) {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        session.delete(thePowerlog);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Deletes all logs for a device id
     *
     * @param deviceId the device id
     */
    public void deletePowerLogs(String deviceId) {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        session.createQuery("delete from Powerlog as powerlog where powerlog.id.deviceid = '" + deviceId + "'").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Returns the unique device ids in the power logs table
     *
     * @return the list of device ids
     */
    public ArrayList<String> getPowerLogDeviceIds() {
        Session session = SESSIONFACTORY.openSession();
        session.beginTransaction();
        List result = session.createQuery("select distinct powerlog.id.deviceid from Powerlog powerlog").list();
        session.getTransaction().commit();
        session.close();

        return (ArrayList) result;
    }
}
