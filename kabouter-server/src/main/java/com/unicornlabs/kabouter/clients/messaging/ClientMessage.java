/*
 * Copyright 2012 Mitch.
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
package com.unicornlabs.kabouter.clients.messaging;

import com.unicornlabs.kabouter.util.JSONUtils;

/**
 *
 * @author Mitch
 */
public class ClientMessage {
    
    public static final String DEVICE_EVENT_MESSAGE = "DEVICE_EVENT_MESSAGE";
    public static final String DEVICE_LIST_MESSAGE = "DEVICE_LIST_MESSAGE";
    public static final String DEVICE_STATUS_MESSAGE = "DEVICE_STATUS_MESSAGE";
    
    public static final String REGISTER_DEVICE_INTEREST_MESSAGE = "REGISTER_DEVICE_INTEREST_MESSAGE";
    
    public String messageType;
    public String data;

    public ClientMessage(String messageType, Object data) {
        this.messageType = messageType;
        this.data = JSONUtils.ToJSON(data);
    }

}
