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
package com.unicornlabs.kabouter.clients.test;

import com.unicornlabs.kabouter.clients.messaging.IOUpdate;
import com.unicornlabs.kabouter.util.JSONUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Mitch
 */
public class TestClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Socket mySocket = new Socket("127.0.0.1", 4646);
        BufferedReader br = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
        
        String line = br.readLine();
        
        System.out.println("line = " + line);
        
        IOUpdate u = new IOUpdate();
        u.deviceId = "test";
        u.ioName = "io";
        u.value = 5;
        
        String lol = JSONUtils.ToJSON(u);

    }
}
