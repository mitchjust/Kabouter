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
package com.unicornlabs.kabouter.devices.test;

import com.unicornlabs.kabouter.devices.DeviceServerMessage;
import com.unicornlabs.kabouter.devices.ServerDeviceMessage;
import com.unicornlabs.kabouter.historian.data_objects.Device;
import com.unicornlabs.kabouter.util.JSONUtils;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TestPowerDevice.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: TODO Add Class Description
 */
public class TestDevice {
    
    private static final Logger LOGGER = Logger.getLogger(TestDevice.class.getName());
    
    static {
        LOGGER.setLevel(Level.ALL);
    }
    private static Socket mySocket;
    private static BufferedReader socketin;
    private static PrintWriter socketout;
    private static BufferedReader br;
    private static Device newDevice;
    private static PowerGenerator myPowerGenerator;
    
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        
        
        String input = "";
        
        while (true) {
            input = br.readLine();
            handleCommand(input);
        }
    }
    
    public static void handleCommand(String command) {
        if (command.equals("quit")) {
            try {
                mySocket.close();
                System.exit(0);
            } catch (IOException ex) {
                System.out.println("ex = " + ex);
            }
        } else if (command.startsWith("connect")) {
            try {
                
                String[] split = command.split(" ");
                
                if (split.length == 3) {
                    
                    mySocket = new Socket(InetAddress.getByName(split[1]), Integer.parseInt(split[2]));
                } else {
                    mySocket = new Socket(InetAddress.getLocalHost(), 4555);
                }
                
                System.out.println("Connection Established");
                
                
                socketin = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                socketout = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));
                
                newDevice = new Device();
                
                newDevice.setDisplayname("Test Device");
                newDevice.setHaspowerlogging(Boolean.TRUE);
                newDevice.setIonames(Arrays.asList(new String[]{"Test IO 1", "Test IO 2"}));
                newDevice.setIpaddress("127.0.0.1");
                newDevice.setNumio(2);
                newDevice.setType("TEST_DEV");
                
                System.out.print("Device ID: ");
                String devid = br.readLine();
                
                newDevice.setId(devid);
                
                DeviceServerMessage newMessage = new DeviceServerMessage();
                newMessage.device = newDevice;
                newMessage.messageType = DeviceServerMessage.DEVICE_CONFIG;
                
                String jsonString = JSONUtils.ToJSON(newMessage);
                
                System.out.println("Sending Config Message");
                
                mySocket.getOutputStream().write(jsonString.getBytes());
                
            } catch (Exception ex) {
                System.out.println("ex = " + ex);
            }
        } else if (command.startsWith("changeio")) {
            try {
                String[] split = command.split(" ");
                int newValue = Integer.parseInt(split[1]);
                
                Integer[] newIo = new Integer[]{newValue, newValue};
                
                DeviceServerMessage newMessage = new DeviceServerMessage();
                newMessage.data = JSONUtils.ToJSON(newIo);
                newMessage.device = newDevice;
                newMessage.messageType = DeviceServerMessage.IO_STATE_CHANGE;
                
                String jsonString = JSONUtils.ToJSON(newMessage);
                
                System.out.println("Sending IO Change Message");
                
                mySocket.getOutputStream().write(jsonString.getBytes());
                
                String newIOMessage = socketin.readLine();
                
                System.out.println("newIOMessage = " + newIOMessage);
                ServerDeviceMessage FromJSON = JSONUtils.FromJSON(newIOMessage, ServerDeviceMessage.class);
                Integer[] newIOStates = JSONUtils.FromJSON(FromJSON.data, Integer[].class);
                
                System.out.println("newIOStates = " + newIOStates[0] + ", " + newIOStates[1]);
                
                
            } catch (IOException ex) {
                System.out.println("ex = " + ex);
            } catch (com.google.gson.JsonSyntaxException jex) {
                System.out.println("Exception: " + jex);
            }
        } else if (command.startsWith("generatepower")) {
            
            String[] split = command.split(" ");
            int newValue = Integer.parseInt(split[1]);
            
            myPowerGenerator = new PowerGenerator(newValue, mySocket);
            myPowerGenerator.start();
            
        } else if (command.startsWith("setbase")) {
            
            String[] split = command.split(" ");
            int newValue = Integer.parseInt(split[1]);
            
            myPowerGenerator.setBaseLevel(newValue);
            
        }
    }
    
    private static class PowerGenerator extends Thread {

        private int baseLevel;
        private Random r;
        private Socket mySocket;
        
        public PowerGenerator(int baseLevel, Socket mySocket) {
            this.baseLevel = baseLevel;
            r = new Random();
            this.mySocket = mySocket;
        }
        
        public void setBaseLevel(int baseLevel) {
            this.baseLevel = baseLevel;
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    float value = r.nextFloat() * (baseLevel / 10) + baseLevel;
                    DeviceServerMessage newMessage = new DeviceServerMessage();
                    newMessage.data = JSONUtils.ToJSON(value);
                    newMessage.device = newDevice;
                    newMessage.messageType = DeviceServerMessage.POWER_LOG;
                    
                    String jsonString = JSONUtils.ToJSON(newMessage);
                    
                    System.out.println("Sending Power Message: " + value);
                    
                    mySocket.getOutputStream().write(jsonString.getBytes());
                    
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TestDevice.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(TestDevice.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
