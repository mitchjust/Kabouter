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
import com.unicornlabs.kabouter.gui.debug.DebugPanel;
import com.unicornlabs.kabouter.gui.power.PowerPanel;
import java.awt.Component;
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

    static {
        LOGGER.setLevel(Level.ALL);
    }
    private MainFrame myMainFrame;
    private MainPanel myMainPanel;
    private PowerPanel myPowerPanel;
    private DebugPanel myDebugPanel;

    public GuiManager() {

        myMainFrame = new MainFrame();
        myMainFrame.setLocationRelativeTo(null);
        myMainFrame.setTitle(KabouterConstants.FRAME_TITLE);

        myMainPanel = new MainPanel();
        
        myPowerPanel = new PowerPanel();
        
        myDebugPanel = new DebugPanel();

        myMainFrame.addTabbedPanel("Home", myMainPanel);
        myMainFrame.addTabbedPanel("Power Logs", myPowerPanel);
        myMainFrame.addTabbedPanel("Debug", myDebugPanel);

    }

    public void initalize() {
        myMainFrame.addTabbedPaneChangeListener(this);
        myMainFrame.setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JTabbedPane pane = (JTabbedPane) e.getSource();
        Component selectedComponent = pane.getSelectedComponent();
        
        if(selectedComponent == myMainPanel) {
            LOGGER.info("Tab changed to Main Panel");
        }
        else if (selectedComponent == myPowerPanel) {
            LOGGER.info("Tab changed to Power Panel");
            myPowerPanel.updateDeviceList();
        }
        else if (selectedComponent == myDebugPanel) {
            LOGGER.info("Tab changed to Debug Panel");
        }
    }
}
