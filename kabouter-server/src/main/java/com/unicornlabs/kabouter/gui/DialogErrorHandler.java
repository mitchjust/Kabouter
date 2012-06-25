// <editor-fold defaultstate="collapsed" desc="License">
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
// </editor-fold>
package com.unicornlabs.kabouter.gui;

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
