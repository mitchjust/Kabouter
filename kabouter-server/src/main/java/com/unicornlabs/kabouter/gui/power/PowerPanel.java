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
import com.unicornlabs.kabouter.devices.DeviceManager;
import com.unicornlabs.kabouter.historian.Historian;
import com.unicornlabs.kabouter.historian.data_objects.Powerlog;
import java.awt.Cursor;
import java.awt.Paint;
import java.awt.Transparency;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Mitch
 */
public class PowerPanel extends javax.swing.JPanel {

    private static final Logger LOGGER = Logger.getLogger(PowerPanel.class.getName());
    private static final int MAX_DATA_POINTS = 1000;
    private static final int MAX_DATA_POINTS_LIVE = 60;
    private boolean liveStatus;
    private Historian theHistorian;
    private DeviceManager theDeviceManager;
    private JFreeChart myChart;
    private HashMap<String,XYSeries> myDataSeriesMap;
    private XYSeriesCollection dataset;

    static {
        LOGGER.setLevel(Level.ALL);
    }

    /**
     * Creates new form PowerPanel
     */
    public PowerPanel() {
        initComponents();
        theHistorian = (Historian) BusinessObjectManager.getBusinessObject(Historian.class.getName());
        theDeviceManager = (DeviceManager) BusinessObjectManager.getBusinessObject(DeviceManager.class.getCanonicalName());
        myDataSeriesMap = new HashMap<String, XYSeries>();
    }

    /**
     * Updates the list of devices
     */
    public void updateDeviceList() {
        deviceList.setEnabled(false);
        //Save the currently selected item
        List selectedValuesList = deviceList.getSelectedValuesList();

        deviceList.removeAll();

        //Get the device ids from the device manager
        ArrayList<String> deviceIds = theDeviceManager.getDeviceDisplayNames();

        //Sort them alphabetically
        Collections.sort(deviceIds);
        
        String[] deviceIdsAsStringArr = new String[deviceIds.size()];
        deviceIdsAsStringArr = deviceIds.toArray(deviceIdsAsStringArr);

        deviceList.setListData(deviceIdsAsStringArr);
        
        for(Object item : selectedValuesList) {
            if(deviceIds.contains((String)item)) {
                deviceList.setSelectedValue(item, true);
            }
        }
        
        deviceList.setEnabled(true);

    }

    /**
     *
     * @param newLog
     */
    public void handleNewPowerLog(Powerlog newLog) {
        //If the new log is from the currently focussed device
        XYSeries focussedSeries = myDataSeriesMap.get(newLog.getId().getDeviceid());
        
        //If we are focussed on this device, the returned series won't be null
        if(focussedSeries != null) {

            //Add the new datapoint
            focussedSeries.add(newLog.getId().getLogtime().getTime(), newLog.getPower());

            //While we have too many data points for a live graph
            while ((long) (newLog.getId().getLogtime().getTime() - focussedSeries.getMinX()) > MAX_DATA_POINTS_LIVE * 1000) {
                //Remove the first data point
                focussedSeries.remove(0);
            }

        }
    }

    /**
     * Returns true if the panel is in live mode
     *
     * @return the status
     */
    public boolean getLiveStatus() {
        return liveStatus;
    }

    /**
     * Sets the chart data and title
     *
     * @param logs the list of power logs
     * @param title the title of the chart
     */
    public void setupChart(ArrayList<Powerlog> logs, String title) {
        
        myDataSeriesMap.clear();
        
        //Create a collection to store the series
        dataset = new XYSeriesCollection();

        //Add each of the logs to the series
        for (Powerlog p : logs) {
            XYSeries deviceSeries = myDataSeriesMap.get(p.getId().getDeviceid());
            
            if(deviceSeries == null) {
                deviceSeries = new XYSeries(p.getId().getDeviceid());
                myDataSeriesMap.put(p.getId().getDeviceid(), deviceSeries);
                dataset.addSeries(deviceSeries);
            }
            
            deviceSeries.add(p.getId().getLogtime().getTime(), p.getPower());
        }

        //Create a custom date axis to display dates on the X axis
        DateAxis dateAxis = new DateAxis("Date");
        //Make the labels vertical
        dateAxis.setVerticalTickLabels(true);

        //Create the power axis
        NumberAxis powerAxis = new NumberAxis("Power");

        //Set both axes to auto range for their values
        powerAxis.setAutoRange(true);
        dateAxis.setAutoRange(true);

        //Create the tooltip generator
        StandardXYToolTipGenerator ttg = new StandardXYToolTipGenerator(
                "{0}: {2}", new SimpleDateFormat("yyyy/MM/dd HH:mm"), NumberFormat.getInstance());


        //Set the renderer
        StandardXYItemRenderer renderer = new StandardXYItemRenderer(
                StandardXYItemRenderer.LINES, ttg, null);

        //Create the plot
        XYPlot plot = new XYPlot(dataset, dateAxis, powerAxis, renderer);
        
        //Create the chart
        myChart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        //Attach the chart to the panel
        ((ChartPanel) chartPanel).setChart(myChart);
        //Set max draw size to 2560x1440
        ((ChartPanel) chartPanel).setMaximumDrawHeight(1440);
        ((ChartPanel) chartPanel).setMaximumDrawWidth(2560);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        chartPanel = new ChartPanel(myChart);
        startDateChooser = new com.toedter.calendar.JDateChooser(new Date());
        jLabel2 = new javax.swing.JLabel();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -6);
        startTimeSpinner = new com.unicornlabs.kabouter.gui.components.JTimeSpinner(cal.getTime());
        endDateChooser = new com.toedter.calendar.JDateChooser(new Date());
        endTimeSpinner = new com.unicornlabs.kabouter.gui.components.JTimeSpinner();
        jLabel3 = new javax.swing.JLabel();
        applyButton = new javax.swing.JButton();
        liveCheckBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        deviceList = new javax.swing.JList();

        jLabel1.setText("Device IDs:");

        chartPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout chartPanelLayout = new javax.swing.GroupLayout(chartPanel);
        chartPanel.setLayout(chartPanelLayout);
        chartPanelLayout.setHorizontalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        chartPanelLayout.setVerticalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 423, Short.MAX_VALUE)
        );

        jLabel2.setText("Graph Start Date:");

        jLabel3.setText("Graph End Date:");

        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        liveCheckBox.setText("Live");
        liveCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                liveCheckBoxActionPerformed(evt);
            }
        });

        deviceList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(deviceList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(startTimeSpinner, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(startDateChooser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                            .addComponent(liveCheckBox))
                        .addGap(73, 73, 73)
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                        .addGap(69, 69, 69)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(applyButton))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(endTimeSpinner, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(endDateChooser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(liveCheckBox)
                        .addGap(39, 39, 39)
                        .addComponent(applyButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(endTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(endDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        liveStatus = liveCheckBox.isSelected();
        List selectedValuesList = deviceList.getSelectedValuesList();

        if (liveStatus == true) {
            Calendar cal = Calendar.getInstance();
            Date currentDate = new Date();
            cal.setTime(currentDate);
            cal.add(Calendar.SECOND, -MAX_DATA_POINTS_LIVE);
            Date startDate = cal.getTime();

            ArrayList<Powerlog> logs = theHistorian.getPowerlogs(selectedValuesList, startDate, currentDate, MAX_DATA_POINTS_LIVE);

            setupChart(logs, "Live Power");


        } else {

            Calendar cal = Calendar.getInstance();
            cal.setTime((Date) startTimeSpinner.getValue());

            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);
            int sec = cal.get(Calendar.SECOND);

            cal.setTime(startDateChooser.getDate());

            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, min);
            cal.set(Calendar.SECOND, sec);

            Date start = cal.getTime();

            cal.setTime((Date) endTimeSpinner.getValue());

            hour = cal.get(Calendar.HOUR_OF_DAY);
            min = cal.get(Calendar.MINUTE);
            sec = cal.get(Calendar.SECOND);

            cal.setTime(endDateChooser.getDate());

            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, min);
            cal.set(Calendar.SECOND, sec);

            Date end = cal.getTime();

            ArrayList<Powerlog> logs = theHistorian.getPowerlogs(selectedValuesList, start, end, MAX_DATA_POINTS);
            
            setupChart(logs, "Power");
        }

        setCursor(Cursor.getDefaultCursor());

    }//GEN-LAST:event_applyButtonActionPerformed

    private void liveCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_liveCheckBoxActionPerformed
        JCheckBox source = (JCheckBox) evt.getSource();
        endTimeSpinner.setEnabled(!source.isSelected());
        endDateChooser.setEnabled(!source.isSelected());
        startTimeSpinner.setEnabled(!source.isSelected());
        startDateChooser.setEnabled(!source.isSelected());
    }//GEN-LAST:event_liveCheckBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyButton;
    private javax.swing.JPanel chartPanel;
    private javax.swing.JList deviceList;
    private com.toedter.calendar.JDateChooser endDateChooser;
    private com.unicornlabs.kabouter.gui.components.JTimeSpinner endTimeSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox liveCheckBox;
    private com.toedter.calendar.JDateChooser startDateChooser;
    private com.unicornlabs.kabouter.gui.components.JTimeSpinner startTimeSpinner;
    // End of variables declaration//GEN-END:variables
}
