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
import com.unicornlabs.kabouter.devices.DeviceManager;
import com.unicornlabs.kabouter.devices.DeviceStatus;
import com.unicornlabs.kabouter.devices.events.DeviceEvent;
import com.unicornlabs.kabouter.devices.events.DeviceEventListener;
import com.unicornlabs.kabouter.devices.events.IOEvent;
import com.unicornlabs.kabouter.historian.Historian;
import com.unicornlabs.kabouter.historian.data_objects.Automationrule;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AutomationManager.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: Controls the automation features
 */
public class AutomationManager implements DeviceEventListener{

    private static final Logger LOGGER = Logger.getLogger(AutomationManager.class.getName());
    private static final String EQUALS_FUNCTION = "EQUALS";
    private static final String MORE_THAN_FUNCTION = "MORE THAN";
    private static final String LESS_THAN_FUNCTION = "LESS THAN";
    public static final String[] FUNCTIONS = {EQUALS_FUNCTION,MORE_THAN_FUNCTION,LESS_THAN_FUNCTION};
    
    static {
        LOGGER.setLevel(Level.ALL);
    }
    
    private DeviceManager theDeviceManager;
    private Historian theHistorian;
    
    public AutomationManager() {
        theDeviceManager = (DeviceManager) BusinessObjectManager.getBusinessObject(DeviceManager.class.getName());
        theHistorian = (Historian) BusinessObjectManager.getBusinessObject(Historian.class.getName());

    }
    
    public ArrayList<Automationrule> getAutomationRules() {
        return theHistorian.getAutomationRules();
    }
    
    public ArrayList<Automationrule> getAutomationRules(String sourceId, String sourceIoName) {
        return theHistorian.getAutomationRules(sourceId, sourceIoName);
    }
    
    public void handleRule(Automationrule rule, double sourceValue) {
        if(rule.getSourceFunction().contentEquals(EQUALS_FUNCTION)) {
            if(rule.getSourceValue() == sourceValue) {
                setIOValue(rule.getTargetId(), rule.getTargetIoName(), rule.getTargetValue());
            }
        } else if(rule.getSourceFunction().contentEquals(MORE_THAN_FUNCTION)) {
            if(sourceValue > rule.getSourceValue()) {
                setIOValue(rule.getTargetId(), rule.getTargetIoName(), rule.getTargetValue());
            }
        } else if(rule.getSourceFunction().contentEquals(LESS_THAN_FUNCTION)) {
            if(sourceValue < rule.getSourceValue()) {
                setIOValue(rule.getTargetId(), rule.getTargetIoName(), rule.getTargetValue());
            }
        } else {
            LOGGER.log(Level.SEVERE, "Unknown Function: {0}", rule.getSourceFunction());
        }
    }
    
    public void setIOValue(String deviceId, String ioName, double value) {
        LOGGER.log(Level.INFO, "Sending IO Update Message To {0} [{1}]:{2}", new Object[]{deviceId, ioName, value});
        DeviceStatus deviceStatus = theDeviceManager.getDeviceStatus(deviceId);
        deviceStatus.tcpChannel.write(ioName+":"+value);
    }

    @Override
    public void handleDeviceEvent(DeviceEvent e) {
        if(e.getEventType().equals(DeviceEvent.IO_CHANGE_EVENT)) {
            DeviceStatus theDeviceInfo = (DeviceStatus) e.getOriginDevice();
            IOEvent theIOEvent = (IOEvent) e.getAttachment();
            
            LOGGER.log(Level.INFO, "Device IO Event Received: {0} {1}", new Object[]{theDeviceInfo.theDevice.getId(), theIOEvent});
            ArrayList<Automationrule> automationRules = getAutomationRules(theDeviceInfo.theDevice.getId(), theIOEvent.getIoName());
            
            for(Automationrule rule : automationRules) {
                handleRule(rule, theIOEvent.getIoValue());
            }
        }
    }
    
}
