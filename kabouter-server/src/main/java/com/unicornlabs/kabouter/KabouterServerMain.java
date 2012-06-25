// <editor-fold defaultstate="collapsed" desc="License">
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
// </editor-fold>
package com.unicornlabs.kabouter;

import com.unicornlabs.kabouter.config.ConfigManager;
import com.unicornlabs.kabouter.config.KabouterConstants;
import com.unicornlabs.kabouter.gui.DialogErrorHandler;
import com.unicornlabs.kabouter.gui.GuiManager;
import com.unicornlabs.kabouter.gui.SplashScreen;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * KabouterServerMain.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: Kick starter for Server
 */
public class KabouterServerMain {

    private static final Logger LOGGER = Logger.getLogger(KabouterServerMain.class.getName());

    static {
        LOGGER.setLevel(Level.ALL);
    }

    public static void main(String[] args) {

        //Display the splash screen
        setGUILookAndFeel();
        SplashScreen mySplashScreen = new SplashScreen();
        mySplashScreen.setTitle(KabouterConstants.FRAME_TITLE);
        //Put it in the middle of the screen and make it visible
        mySplashScreen.setLocationRelativeTo(null);
        mySplashScreen.setVisible(true);
        mySplashScreen.addText("Starting Kabouter Server...");

        try {
            //Do inital config
            mySplashScreen.addText("Starting Logging Service");
            KabouterServerMain.setupAppDirectory();
            KabouterServerMain.configureLogging();

            //Setup the Config Manager
            mySplashScreen.addText("Reading Kabouter Config");
            InputStream myPropertiesStream =
                    KabouterServerMain.class.getResourceAsStream(
                    KabouterConstants.CONFIG_PATH
                    + KabouterConstants.KABOUTER_SERVER_PROPERTIES);
            ConfigManager theConfigManager = new ConfigManager(myPropertiesStream);

            //Start the module manager and register it in the BOM
            mySplashScreen.addText("Starting Module Manager");

            mySplashScreen.fade();

            //Start the GUI Manager in the Swing Initializer Thread
            mySplashScreen.addText("Starting GUI Manager");
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    GuiManager theGuiManager = new GuiManager();
                    BusinessObjectManager.registerBusinessObject(GuiManager.class.getName(), theGuiManager);
                    theGuiManager.initalize();
                }
            });



        } catch (IOException ex) {
            mySplashScreen.addText("Exception In Startup: " + ex);
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sets up the logger for the entire application
     *
     * @throws IOException
     */
    public static void configureLogging() throws IOException {
        //Read the logging config file
        InputStream loggingProperties = KabouterServerMain.class.getResourceAsStream(KabouterConstants.CONFIG_PATH + KabouterConstants.LOGGING_PROPERTIES);
        LogManager.getLogManager().readConfiguration(loggingProperties);

        Logger rootLogger = Logger.getLogger("");
        //Add a popup handler for severe messages
        rootLogger.addHandler(new DialogErrorHandler());

    }

    /**
     * Set the look and feel to nimbus
     */
    public static void setGUILookAndFeel() {

        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(KabouterServerMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(KabouterServerMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(KabouterServerMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(KabouterServerMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    /**
     * Sets up the home directory to store logs
     *
     * @return
     */
    public static boolean setupAppDirectory() {
        File myAppDirectory = new File(KabouterConstants.APP_DIRECTORY);
        if (myAppDirectory.exists()) {
            return true;
        } else {
            boolean success = myAppDirectory.mkdir();
            if (!success) {
                System.err.println("Unable to create directory: " + myAppDirectory);
            }
            return success;
        }
    }
}
