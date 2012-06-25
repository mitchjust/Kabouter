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
package com.unicornlabs.kabouter.gui.power;

import com.unicornlabs.kabouter.BusinessObjectManager;
import com.unicornlabs.kabouter.historian.Historian;
import java.util.ArrayList;
import java.util.Collections;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author Mitch
 */
public class PowerPanel extends javax.swing.JPanel {

    JFreeChart myChart;
    
    /**
     * Creates new form PowerPanel
     */
    public PowerPanel() {
        initComponents();
    }
    
    public void updateDeviceList() {
        deviceComboBox.removeAllItems();
        
        Historian theHistorian = (Historian) BusinessObjectManager.getBusinessObject(Historian.class.getName());
        ArrayList<String> powerLogDeviceIds = theHistorian.getPowerLogDeviceIds();
        
        Collections.sort(powerLogDeviceIds);
        
        for(String s: powerLogDeviceIds) {
            deviceComboBox.addItem(s);
        }
        
        deviceComboBox.setSelectedIndex(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        deviceComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();

        jLabel1.setText("Device ID:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deviceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(654, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deviceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(586, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox deviceComboBox;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
