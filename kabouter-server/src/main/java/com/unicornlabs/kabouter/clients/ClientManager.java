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

package com.unicornlabs.kabouter.clients;

import com.unicornlabs.kabouter.net.TCPChannelServer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ClientManager.java
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description:
 * Handles information requests from clients
 */

public class ClientManager {

    private static final Logger LOGGER = Logger.getLogger(ClientManager.class.getName());
    
    static{
        LOGGER.setLevel(Level.ALL);
    }
    
    private TCPChannelServer myTCPChannelServer;
    private int myPort;

    public ClientManager(int myPort) {
        this.myPort = myPort;
    }
    
    public void startServer() {
        myTCPChannelServer = new TCPChannelServer(myPort, new KabouterClientPipelineFactory());
        myTCPChannelServer.run();
    }

}