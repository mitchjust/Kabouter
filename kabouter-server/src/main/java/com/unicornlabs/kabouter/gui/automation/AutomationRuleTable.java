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
package com.unicornlabs.kabouter.gui.automation;

import com.unicornlabs.kabouter.BusinessObjectManager;
import com.unicornlabs.kabouter.automation.AutomationManager;
import com.unicornlabs.kabouter.historian.data_objects.Automationrule;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mitch
 */
public class AutomationRuleTable extends JTable{
    
    private static final Logger LOGGER = Logger.getLogger(AutomationRuleTable.class.getName());
    private static final String[] columnNames = {
        "Source Device", 
        "Source IO", 
        "Source Value",
        "Function",
        "Target Device",
        "Target IO",
        "Target Value"};
    private static final Class[] types = new Class[]{
        java.lang.String.class,
        java.lang.String.class,
        java.lang.Double.class,
        java.lang.String.class,
        java.lang.String.class,
        java.lang.String.class,
        java.lang.Double.class};

    static {
        LOGGER.setLevel(Level.ALL);
    }
    
    private DefaultTableModel myModel;
    private AutomationManager theAutomationManager;
    private ArrayList<Automationrule> automationRules;

    public AutomationRuleTable() {
        theAutomationManager = (AutomationManager) BusinessObjectManager.getBusinessObject(AutomationManager.class.getName());
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
        
        automationRules = theAutomationManager.getAutomationRules();
        myModel.setRowCount(0);
        for (Automationrule rule : automationRules) {
            myModel.addRow(new Object[]{rule.getSourceId(),
                rule.getSourceIoName(),
                rule.getSourceValue(),
                rule.getSourceFunction(),
                rule.getTargetId(),
                rule.getTargetIoName(),
                rule.getTargetValue()
            });
        }
    }
    
    public Automationrule getSelectedRule() {
        int leadSelectionIndex = this.getSelectionModel().getLeadSelectionIndex();
        
        if(leadSelectionIndex == -1) {
            //Table is updating
            return null;
        }
        
        return automationRules.get(leadSelectionIndex);
    }

}
