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

import com.unicornlabs.kabouter.BusinessObjectManager;
import com.unicornlabs.kabouter.config.KabouterConstants;
import com.unicornlabs.kabouter.devices.DeviceStatus;
import com.unicornlabs.kabouter.devices.DeviceManager;
import com.unicornlabs.kabouter.devices.events.DeviceEvent;
import com.unicornlabs.kabouter.devices.events.DeviceEventListener;
import com.unicornlabs.kabouter.gui.debug.DebugPanel;
import com.unicornlabs.kabouter.gui.power.PowerPanel;
import com.unicornlabs.kabouter.historian.data_objects.Powerlog;
import java.awt.Component;
import java.awt.Frame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * GuiManager.java
 *
 * @author Mitch
 *
 * Description: Handles GUI functions
 */
public class GuiManager implements ChangeListener, DeviceEventListener {

    private static final Logger LOGGER = Logger.getLogger(GuiManager.class.getName());

    static {
        LOGGER.setLevel(Level.ALL);
    }
    private DeviceManager theDeviceManager;
    private MainFrame myMainFrame;
    private MainPanel myMainPanel;
    private PowerPanel myPowerPanel;
    private DebugPanel myDebugPanel;

    /**
     * Obtain business objects and setup gui objects
     */
    public GuiManager() {

        //Get device mangaer
        theDeviceManager = (DeviceManager) BusinessObjectManager.getBusinessObject(DeviceManager.class.getName());

        //Setup main frame
        myMainFrame = new MainFrame();
        myMainFrame.setLocationRelativeTo(null);
        myMainFrame.setTitle(KabouterConstants.FRAME_TITLE);

        //Setup panels
        myMainPanel = new MainPanel();

        myPowerPanel = new PowerPanel();

        myDebugPanel = new DebugPanel();

        //Add panels as tabs
        myMainFrame.addTabbedPanel("Home", myMainPanel);
        myMainFrame.addTabbedPanel("Power Logs", myPowerPanel);
        myMainFrame.addTabbedPanel("Debug", myDebugPanel);

    }

    /**
     * Attach to event sources
     */
    public void initalize() {
        myMainFrame.addTabbedPaneChangeListener(this);
        theDeviceManager.addDeviceEventListener(this);
        myMainFrame.setVisible(true);
        //Start with frame maximized
        myMainFrame.setExtendedState(myMainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Indicates that the selected tab has changed
     *
     * @param e
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        JTabbedPane pane = (JTabbedPane) e.getSource();

        Component selectedComponent = pane.getSelectedComponent();

        if (selectedComponent == myMainPanel) {
            LOGGER.info("Tab changed to Main Panel");
        } else if (selectedComponent == myPowerPanel) {
            LOGGER.info("Tab changed to Power Panel");
            myPowerPanel.updateDeviceList();
        } else if (selectedComponent == myDebugPanel) {
            LOGGER.info("Tab changed to Debug Panel");
        }
    }

    /**
     * Performs an action based on event type and selected pane in the swing
     * thread
     *
     * @param e the event
     */
    @Override
    public void handleDeviceEvent(final DeviceEvent e) {

        //Only execute gui operations in the swing thread
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                if (e.getEventType().equals(DeviceEvent.NEW_DEVICE_EVENT)) {
                    //Actions for New Power Logs
                    if (myMainFrame.getCurrentTab() == myPowerPanel) {
                        //Action for power panel
                        myPowerPanel.updateDeviceList();
                    }
                } else if (e.getEventType().equals(DeviceEvent.POWER_LOG_EVENT)) {
                    //Actions for New Power Logs
                    if (myMainFrame.getCurrentTab() == myPowerPanel) {
                        //Action for power panel
                        
                        //If the power panel is in live mode, update it
                        if (myPowerPanel.getLiveStatus() == true) {
                            Powerlog newLog = (Powerlog) e.getAttachment();
                            myPowerPanel.handleNewPowerLog(newLog);
                        }
                    }
                }
            }
        });

    }
}
