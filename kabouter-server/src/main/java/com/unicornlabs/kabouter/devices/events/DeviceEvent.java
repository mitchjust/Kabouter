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
package com.unicornlabs.kabouter.devices.events;

import com.unicornlabs.kabouter.devices.DeviceStatus;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * NewClass.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: Event from device
 */
public class DeviceEvent extends EventObject {

    private static final Logger LOGGER = Logger.getLogger(DeviceEvent.class.getName());

    static {
        LOGGER.setLevel(Level.ALL);
    }
    //Event types
    public static final String DEVICE_CONNECTION_EVENT = "DEVICE_CONNECTION_EVENT";
    public static final String DEVICE_DISCONNECTION_EVENT = "DEVICE_DISCONNECTION_EVENT";
    public static final String IO_CHANGE_EVENT = "IO_CHANGE_EVENT";
    public static final String POWER_LOG_EVENT = "POWER_LOG_EVENT";
    
    //Attached objects
    private Object attachment;
    private DeviceStatus originDevice;
    
    //The event type
    private String eventType;

    /**
     * Constructor
     * @param o source of event
     * @param eventType type of event
     * @param attachment attachment
     */
    public DeviceEvent(Object o, String eventType, DeviceStatus originDevice, Object attachment) {
        super(o);
        this.eventType = eventType;
        this.attachment = attachment;
        this.originDevice = originDevice;
    }

    /**
     * Constructor with null attachment
     * @param o source of event
     * @param eventType type of event
     */
    public DeviceEvent(Object o, String eventType, DeviceStatus originDevice) {
        this(o, eventType, originDevice, null);
    }

    /**
     * Get the attachment
     * @return the attachment
     */
    public Object getAttachment() {
        return attachment;
    }

    /**
     * Get the event type
     * @return the event type
     */
    public String getEventType() {
        return eventType;
    }

    public DeviceStatus getOriginDevice() {
        return originDevice;
    }
    
    
}
