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
package com.unicornlabs.kabouter.gui.devices;

import com.unicornlabs.kabouter.BusinessObjectManager;
import com.unicornlabs.kabouter.devices.DeviceManager;
import com.unicornlabs.kabouter.devices.DeviceStatus;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * DeviceTable.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: TODO Add Class Description
 */
public class DeviceTable extends JTable {

    private static final Logger LOGGER = Logger.getLogger(DeviceTable.class.getName());
    private static final String[] columnNames = {"Device", "Type", "Connected"};
    private static final Class[] types = new Class[]{java.lang.String.class,
        java.lang.String.class,
        java.lang.Boolean.class};

    static {
        LOGGER.setLevel(Level.ALL);
    }
    private DefaultTableModel myModel;
    private DeviceManager theDeviceManager;

    public DeviceTable() {
        theDeviceManager = (DeviceManager) BusinessObjectManager.getBusinessObject(DeviceManager.class.getName());
        myModel = (DefaultTableModel) getModel();
        myModel.setColumnIdentifiers(columnNames);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    public void updateTableData() {
        
        DeviceStatus[] data = theDeviceManager.getDeviceStatuses();
        myModel.setRowCount(0);
        for (DeviceStatus d : data) {
            myModel.addRow(new Object[]{d.theDevice.getDisplayname(),
                        d.theDevice.getType(),
                        d.isConnected});
        }
    }
}
