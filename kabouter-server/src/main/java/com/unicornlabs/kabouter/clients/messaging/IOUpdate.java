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

import java.io.Serializable;

/**
 *
 * @author Mitch
 */
public class IOUpdate implements Serializable {

    public String deviceId;
    public String ioName;
    public float value;

    @Override
    public String toString() {
        return "IOUpdate [deviceId=" + deviceId + ", ioName=" + ioName
                + ", value=" + value + "]";
    }
}
