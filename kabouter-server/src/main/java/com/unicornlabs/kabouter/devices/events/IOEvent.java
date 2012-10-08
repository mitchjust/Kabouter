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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * IOEvent.java
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description:
 * TODO Add Class Description
 */

public class IOEvent {

    private static final Logger LOGGER = Logger.getLogger(IOEvent.class.getName());
    
    static{
        LOGGER.setLevel(Level.ALL);
    }
    
    private String ioName;
    private double ioValue;

    public IOEvent(String ioName, double ioValue) {
        this.ioName = ioName;
        this.ioValue = ioValue;
    }

    public String getIoName() {
        return ioName;
    }

    public void setIoName(String ioName) {
        this.ioName = ioName;
    }

    public double getIoValue() {
        return ioValue;
    }

    public void setIoValue(double ioValue) {
        this.ioValue = ioValue;
    }
    
    @Override
    public String toString() {
        return "{" + ioName + "}: " + ioValue;
    }
    
    

}
