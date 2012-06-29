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

import com.unicornlabs.kabouter.historian.Historian;
import com.unicornlabs.kabouter.historian.data_objects.Powerlog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Mitch
 */
public class ChartTestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        Historian theHistorian = new Historian();

        Calendar theCalendar = Calendar.getInstance();

        Date currentDate = theCalendar.getTime();
        theCalendar.add(Calendar.MINUTE, -20);
        Date yesterday = theCalendar.getTime();

        ArrayList<Powerlog> logs = theHistorian.getPowerlogs("PDEV_001", yesterday, currentDate);

        System.out.println(logs.size() + " Entries betweed " + yesterday + " and " + currentDate);

        XYSeries series = new XYSeries("First");

        for (Powerlog p : logs) {
            series.add(p.getId().getLogtime().getTime(), p.getPower());
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        
        JFreeChart chart = ChartFactory.createXYLineChart("Power Usage", "Date", "Power", dataset, PlotOrientation.VERTICAL, true, true, true);
        
        ChartFrame frame = new ChartFrame("Power",chart);
        frame.pack();
        frame.setVisible(true);
        
        while(true) {
            Thread.sleep(100);
            
            double newX = series.getMaxX() + 1000;
            double newY = 5000;
            System.out.println("Trying To add " + newX + " " + newY);
            series.add(newX, newY);
            series.remove(0);
        }
        
        
    }
}
