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

import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * NewClass.java
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description:
 * TODO Add Class Description
 */

public class DeviceEvent extends EventObject{

    private static final Logger LOGGER = Logger.getLogger(DeviceEvent.class.getName());
    
    static{
        LOGGER.setLevel(Level.ALL);
    }
    
    public static final String NEW_DEVICE_EVENT = "NEW_DEVICE_EVENT";
    public static final String POWER_LOG_EVENT = "POWER_LOG_EVENT";
    
    private Object attachment;
    private String eventType;

    public DeviceEvent(Object o, String eventType, Object attachment) {
        super(o);
        this.eventType = eventType;
        this.attachment = attachment;
    }
    
    public DeviceEvent(Object o, String eventType) {
        this(o,eventType,null);
    }

    public Object getAttachment() {
        return attachment;
    }

    public String getEventType() {
        return eventType;
    }
}
