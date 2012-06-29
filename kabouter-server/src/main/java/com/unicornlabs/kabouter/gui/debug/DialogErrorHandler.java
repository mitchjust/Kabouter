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
package com.unicornlabs.kabouter.gui.debug;

import java.util.logging.*;
import javax.swing.JOptionPane;

/**
 * DialogExceptionHandler.java
 *
 * @author Mitch
 *
 * Description: Displays Severe Log Items in a popup dialog
 */
public class DialogErrorHandler extends Handler {

    private static final Logger LOGGER = Logger.getLogger(DialogErrorHandler.class.getName());

    static {
        LOGGER.setLevel(Level.ALL);
    }

    /**
     * Constructor
     */
    public DialogErrorHandler() {
        //Set the formatter to a simple formatter
        setFormatter(new SimpleFormatter());
    }

    /**
     * Displays severs log items
     *
     * @param record
     */
    @Override
    public void publish(LogRecord record) {
        //If the level is severe
        if (record.getLevel() == Level.SEVERE) {
            //Format it and display it
            String logMessage = getFormatter().format(record);
            JOptionPane.showMessageDialog(null, logMessage, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * handler doesn't store data, not necessary
     */
    @Override
    public void flush() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * handler doesn't store data, not necessary
     */
    @Override
    public void close() throws SecurityException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
