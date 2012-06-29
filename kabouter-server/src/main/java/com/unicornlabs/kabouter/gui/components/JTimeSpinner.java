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
package com.unicornlabs.kabouter.gui.components;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * JTimeSpinner.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: JSpinner for entering times
 */
public class JTimeSpinner extends JSpinner {

    private static final Logger LOGGER = Logger.getLogger(JTimeSpinner.class.getName());

    static {
        LOGGER.setLevel(Level.ALL);
    }

    /**
     * Create JTimeSpinner with current date
     */
    public JTimeSpinner() {
        this(new Date());
    }

    /**
     * Create JTimeSpinner with an initial value
     * @param initalValue 
     */
    public JTimeSpinner(Date initalValue) {
        super(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(this, "HH:mm:ss");
        setEditor(timeEditor);
        setValue(initalValue);
    }
}
