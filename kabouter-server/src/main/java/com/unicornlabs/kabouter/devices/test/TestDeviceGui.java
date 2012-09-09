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
package com.unicornlabs.kabouter.devices.test;

import com.unicornlabs.kabouter.devices.messaging.DeviceServerMessage;
import com.unicornlabs.kabouter.util.JSONUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mitch
 */
public class TestDeviceGui extends javax.swing.JFrame {

    private Socket mySocket;
    private BufferedReader socketin;
    private PrintWriter socketout;
    private PowerGenerator myPowerGenerator;

    /**
     * Creates new form TestDeviceGui
     */
    public TestDeviceGui() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        idField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        connectButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputArea = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        powerField = new javax.swing.JTextField();
        updateButton = new javax.swing.JButton();
        runButton = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        idField.setText("Kabouter Device");

        jLabel1.setText("Device ID:");

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        outputArea.setColumns(20);
        outputArea.setRows(5);
        jScrollPane1.setViewportView(outputArea);

        jLabel2.setText("Power Base:");

        powerField.setText("1000");
        powerField.setEnabled(false);

        updateButton.setText("Update");
        updateButton.setEnabled(false);
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        runButton.setText("Run");
        runButton.setEnabled(false);
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(powerField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(updateButton))
                            .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(connectButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(runButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(connectButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(powerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateButton)
                    .addComponent(runButton))
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        try {
            output("Connecting To localhost:4555");
            mySocket = new Socket(InetAddress.getLocalHost(), 4555);
            socketin = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            socketout = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));

            output("Connection Established");

            DeviceServerMessage newMessage = new DeviceServerMessage();

            String devId = idField.getText();

            newMessage.messageType = DeviceServerMessage.DEVICE_CONFIG;
            newMessage.data = devId + ":KABOUTER_TEST_DEVICE";

            String jsonString = JSONUtils.ToJSON(newMessage);

            output("Sending Config Message");

            output("jsonString = " + jsonString);

            mySocket.getOutputStream().write(jsonString.getBytes());

            connectButton.setEnabled(false);
            idField.setEnabled(false);
            powerField.setEnabled(true);
            runButton.setEnabled(true);

        } catch (IOException ex) {
            output(ex.getMessage());
        }
    }//GEN-LAST:event_connectButtonActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        int powerLevel;
        try {
            powerLevel = Integer.parseInt(powerField.getText());
        } catch (NumberFormatException nfe) {
            powerLevel = 0;
        }
        myPowerGenerator.setBaseLevel(powerLevel);
    }//GEN-LAST:event_updateButtonActionPerformed

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
        boolean run = runButton.isSelected();
        if (run) {
            int powerLevel;
            try {
                powerLevel = Integer.parseInt(powerField.getText());
            } catch (NumberFormatException nfe) {
                powerLevel = 0;
            }

            myPowerGenerator = new PowerGenerator(powerLevel, mySocket, this);
            myPowerGenerator.start();
            
            updateButton.setEnabled(true);

        } else {
            myPowerGenerator.stopThread();
            updateButton.setEnabled(false);
        }
    }//GEN-LAST:event_runButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TestDeviceGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TestDeviceGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TestDeviceGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TestDeviceGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TestDeviceGui().setVisible(true);
            }
        });
    }

    public void output(String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    private static class PowerGenerator extends Thread {

        private int baseLevel;
        private Random r;
        private Socket mySocket;
        private TestDeviceGui parent;
        private boolean alive;

        public PowerGenerator(int baseLevel, Socket mySocket, TestDeviceGui parent) {
            this.baseLevel = baseLevel;
            r = new Random();
            this.mySocket = mySocket;
            this.parent = parent;
            parent.output("Power Thread Linked To GUI");
            alive = true;
        }

        public void setBaseLevel(int baseLevel) {
            parent.output("Changing Base Level To " + baseLevel);
            this.baseLevel = baseLevel;
        }

        public void stopThread() {
            alive = false;
        }

        @Override
        public void run() {
            while (alive) {
                try {
                    float value = r.nextFloat() * (baseLevel / 10) + baseLevel;
                    DeviceServerMessage newMessage = new DeviceServerMessage();
                    newMessage.data = value;
                    newMessage.messageType = DeviceServerMessage.POWER_LOG;

                    String jsonString = JSONUtils.ToJSON(newMessage);

                    parent.output("Sending Power Message: " + value);
                    parent.output("jsonString = " + jsonString);

                    mySocket.getOutputStream().write(jsonString.getBytes());

                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TestDevice.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(TestDevice.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            parent.output("Stopping Power Thread");
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectButton;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea outputArea;
    private javax.swing.JTextField powerField;
    private javax.swing.JToggleButton runButton;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}
