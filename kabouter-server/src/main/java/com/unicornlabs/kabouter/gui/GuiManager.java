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
package com.unicornlabs.kabouter.gui;

import com.unicornlabs.kabouter.config.KabouterConstants;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * GuiManager.java
 *
 * @author Mitch
 *
 * Description: Handles GUI functions
 */
public class GuiManager implements ChangeListener {

    private static final Logger LOGGER = Logger.getLogger(GuiManager.class.getName());
    public static final int HOME_TAB_INDEX = 0;
    public static final int MODULE_TAB_INDEX = 1;

    static {
        LOGGER.setLevel(Level.ALL);
    }
    private MainFrame myMainFrame;
    private MainPanel myMainPanel;

    public GuiManager() {

        myMainFrame = new MainFrame();
        myMainFrame.setLocationRelativeTo(null);
        myMainFrame.setTitle(KabouterConstants.FRAME_TITLE);

        myMainPanel = new MainPanel();

        myMainFrame.addTabbedPanel("Home", myMainPanel);

    }

    public void initalize() {
        myMainFrame.addTabbedPaneChangeListener(this);
        myMainFrame.setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JTabbedPane pane = (JTabbedPane) e.getSource();
        switch (pane.getSelectedIndex()) {
            case HOME_TAB_INDEX:
                break;
            case MODULE_TAB_INDEX:
                break;
            default:
                LOGGER.severe("Switched To Unknown Tab!");
        }
    }
}
