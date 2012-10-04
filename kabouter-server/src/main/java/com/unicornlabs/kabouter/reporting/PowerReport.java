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
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.unicornlabs.kabouter.BusinessObjectManager;
import com.unicornlabs.kabouter.historian.Historian;
import com.unicornlabs.kabouter.historian.data_objects.Powerlog;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * PowerReport.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: TODO Add Class Description
 */
public class PowerReport {

    private static final Logger LOGGER = Logger.getLogger(PowerReport.class.getName());

    static {
        LOGGER.setLevel(Level.ALL);
    }

    public static void GeneratePowerReport(Date startDate, Date endDate) {
        try {
            Historian theHistorian = (Historian) BusinessObjectManager.getBusinessObject(Historian.class.getName());
            ArrayList<String> powerLogDeviceIds = theHistorian.getPowerLogDeviceIds();

            Document document = new Document(PageSize.A4, 50, 50, 50, 50);

            File outputFile = new File("PowerReport.pdf");
            outputFile.createNewFile();

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFile));
            document.open();

            document.add(new Paragraph("Power Report for " + startDate.toString() + " to " + endDate.toString()));
            
            document.newPage();

            DecimalFormat df = new DecimalFormat("#.###");

            for (String deviceId : powerLogDeviceIds) {
                ArrayList<Powerlog> powerlogs = theHistorian.getPowerlogs(deviceId, startDate, endDate);
                double total = 0;
                double max = 0;
                Date maxTime = startDate;
                double average = 0;
                XYSeries series = new XYSeries(deviceId);
                XYDataset dataset = new XYSeriesCollection(series);

                for (Powerlog log : powerlogs) {
                    total += log.getPower();
                    if (log.getPower() > max) {
                        max = log.getPower();
                        maxTime = log.getId().getLogtime();
                    }
                    series.add(log.getId().getLogtime().getTime(), log.getPower());
                }

                average = total / powerlogs.size();

                document.add(new Paragraph("\nDevice: " + deviceId));
                document.add(new Paragraph("Average Power Usage: " + df.format(average)));
                document.add(new Paragraph("Maximum Power Usage: " + df.format(max) + " at " + maxTime.toString()));
                document.add(new Paragraph("Total Power Usage: " + df.format(total)));
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
                JFreeChart myChart = new JFreeChart(deviceId, JFreeChart.DEFAULT_TITLE_FONT, plot, true);

                PdfContentByte pcb = writer.getDirectContent();
                PdfTemplate tp = pcb.createTemplate(480, 360);
                Graphics2D g2d = tp.createGraphics(480, 360, new DefaultFontMapper());
                Rectangle2D r2d = new Rectangle2D.Double(0, 0, 480, 360);
                myChart.draw(g2d, r2d);
                g2d.dispose();
                pcb.addTemplate(tp, 0, 0);
                
                document.newPage();
            }

            document.close();

            JOptionPane.showMessageDialog(null, "Report Generated.");

            Desktop.getDesktop().open(outputFile);
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(null, "Unable To Open File For Writing, Make Sure It Is Not Currently Open");
        } catch (IOException ex) {
            Logger.getLogger(PowerReport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(PowerReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
