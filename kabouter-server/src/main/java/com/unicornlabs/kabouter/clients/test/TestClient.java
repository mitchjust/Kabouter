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

package com.unicornlabs.kabouter.clients.test;

import com.unicornlabs.kabouter.clients.ClientServerMessage;
import com.unicornlabs.kabouter.devices.DeviceInfo;
import com.unicornlabs.kabouter.util.JSONUtils;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TestClient.java
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description:
 * TODO Add Class Description
 */

public class TestClient {

    private static final Logger LOGGER = Logger.getLogger(TestClient.class.getName());
    
    static{
        LOGGER.setLevel(Level.ALL);
    }
    
    private static Socket mySocket;
    private static BufferedReader socketin;
    private static PrintWriter socketout;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        
        String input = "";
        
        while(true) {
            input = br.readLine();
            handleCommand(input);
        }
    }
    
    public static void handleCommand(String command) {
        if(command.equals("quit")) {
            try {
                mySocket.close();
                System.exit(0);
            } catch (IOException ex) {
                System.out.println("ex = " + ex);
            }
        }
        
        else if(command.equals("connect")) {
            try {
                mySocket = new Socket(InetAddress.getLocalHost(), 4646);
                System.out.println("Connection Established");
                
                socketin = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                socketout = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));
                
                String deviceListJson = socketin.readLine();
                
                System.out.println("deviceListJson = " + deviceListJson);
                DeviceInfo[] FromJSON = JSONUtils.FromJSON(deviceListJson, DeviceInfo[].class);
                
                System.out.println("FromJSON.length = " + FromJSON.length);
                
                for(DeviceInfo d : FromJSON) {
                    System.out.println("Device:\n" + d);
                }
            } catch (Exception ex) {
                System.out.println("ex = " + ex);
            }
        }
        
        else if(command.startsWith("getinfo")) {
            try {
                String[] split = command.split(" ");
                String deviceId = split[1];
                System.out.println("Requesting Device Info for ID = " + deviceId);
                
                ClientServerMessage newClientServerMessage = new ClientServerMessage();
                newClientServerMessage.deviceId = deviceId;
                newClientServerMessage.messageType = ClientServerMessage.DEVICE_INFO_REQUEST;
                
                String jsonString = JSONUtils.ToJSON(newClientServerMessage);
                
                System.out.println("jsonString = " + jsonString);
                
                mySocket.getOutputStream().write(jsonString.getBytes());
                
                String deviceInfo = socketin.readLine();
                    
                System.out.println("deviceInfo = " + deviceInfo);
                DeviceInfo FromJSON = JSONUtils.FromJSON(deviceInfo, DeviceInfo.class);
                    
                System.out.println("FromJSON = " + FromJSON);
            } catch (IOException ex) {
                System.out.println("ex = " + ex);
            } catch (com.google.gson.JsonSyntaxException jex) {
                System.out.println("Exception: " + jex);
            }
        }
    }

}
