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

import com.unicornlabs.kabouter.clients.messaging.ClientMessage;
import com.unicornlabs.kabouter.devices.DeviceStatus;
import com.unicornlabs.kabouter.util.JSONUtils;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TestClient.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: TODO Add Class Description
 */
public class TestClient {

    private static final Logger LOGGER = Logger.getLogger(TestClient.class.getName());

    static {
        LOGGER.setLevel(Level.ALL);
    }
    private static Socket mySocket;
    private static BufferedReader socketin;
    private static PrintWriter socketout;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


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
                    mySocket = new Socket(InetAddress.getLocalHost(), 4646);
                }

                System.out.println("Connection Established");


                socketin = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                socketout = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));

                String deviceListJson = socketin.readLine();

                System.out.println("deviceListJson = " + deviceListJson);
                ClientMessage FromJSON = JSONUtils.FromJSON(deviceListJson, ClientMessage.class);

                System.out.println("FromJSON.messageType = " + FromJSON.messageType);
                System.out.println("FromJSON.data = " + FromJSON.data);
                DeviceStatus[] FromJSON1 = JSONUtils.FromJSON(FromJSON.data, DeviceStatus[].class);

                for (DeviceStatus d : FromJSON1) {
                    System.out.println("d = " + d);
                }

            } catch (Exception ex) {
                System.out.println("ex = " + ex);
            }
        } else if (command.startsWith("register")) {
            try {
                String[] split = command.split(" ");

                if (split.length != 2) {
                    System.out.println("errrrr");
                }

                String device = split[1];

                System.out.println("Registering To Device " + device);

                ClientMessage message = new ClientMessage(ClientMessage.REGISTER_DEVICE_INTEREST_MESSAGE, device);
                String ToJSON = JSONUtils.ToJSON(message);

                System.out.println("ToJSON = " + ToJSON);

                mySocket.getOutputStream().write(ToJSON.getBytes());
            } catch (Exception ex) {
                System.out.println("ex = " + ex);
            }
        }

    }
}
