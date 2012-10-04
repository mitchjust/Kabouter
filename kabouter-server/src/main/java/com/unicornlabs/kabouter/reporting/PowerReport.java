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

package com.unicornlabs.kabouter.reporting;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.unicornlabs.kabouter.BusinessObjectManager;
import com.unicornlabs.kabouter.historian.Historian;
import com.unicornlabs.kabouter.historian.data_objects.Powerlog;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * PowerReport.java
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description:
 * TODO Add Class Description
 */

public class PowerReport {

    private static final Logger LOGGER = Logger.getLogger(PowerReport.class.getName());
    
    static{
        LOGGER.setLevel(Level.ALL);
    }
    
    public static void GeneratePowerReport(Date startDate, Date endDate) {
        try {
            Historian theHistorian = (Historian) BusinessObjectManager.getBusinessObject(Historian.class.getName());
            ArrayList<String> powerLogDeviceIds = theHistorian.getPowerLogDeviceIds();
            
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Power Report.pdf"));
            document.open();
            
            document.add(new Paragraph("Power Report for " + startDate.toString() + " to " + endDate.toString()));
            
            for(String deviceId : powerLogDeviceIds) {
                ArrayList<Powerlog> powerlogs = theHistorian.getPowerlogs(deviceId, startDate, endDate);
                double total = 0;
                double max = 0;
                Date maxTime = startDate;
                double average = 0;
                
                for(Powerlog log : powerlogs) {
                    total += log.getPower();
                    if(log.getPower() > max) {
                        max = log.getPower();
                        maxTime = log.getId().getLogtime();
                    }
                }
                
                average = total/powerlogs.size();
                
                document.add(new Paragraph("\nDevice: " + deviceId));
                document.add(new Paragraph("Average Power Usage: " + average));
                document.add(new Paragraph("Maximum Power Usage: " + max + " at " + maxTime.toString()));
                document.add(new Paragraph("Total Power Usage: " + total));
            }
            
            document.close();
            
            JOptionPane.showMessageDialog(null, "Report Generated.");
            
        } catch (DocumentException ex) {
            Logger.getLogger(PowerReport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PowerReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
