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

package com.unicornlabs.kabouter.net;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * TCPChannelServer.java
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description:
 * Generic Netty TCP Channel nio server
 */

public class TCPChannelServer {

    private static final Logger LOGGER = Logger.getLogger(TCPChannelServer.class.getName());
    
    static{
        LOGGER.setLevel(Level.ALL);
    }
    
    /**
     * the port to listen on
     */
    private int port;
    
    /**
     * the server bootstrap
     */
    private ServerBootstrap myBootstrap;
    
    /**
     * the pipeline factory
     */
    private ChannelPipelineFactory myFactory;

    /**
     * Set the initial parameters
     * @param port the port to listen on
     * @param myFactory the factory to use to create pipelines
     */
    public TCPChannelServer(int port, ChannelPipelineFactory myFactory) {
        this.port = port;
        this.myFactory = myFactory;
    }
    
    /**
     * Start the server
     */
    public void run() {
        //Create bootstrap with cached executors
        myBootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(), 
                Executors.newCachedThreadPool()));
        
        myBootstrap.setPipelineFactory(myFactory);
        
        myBootstrap.bind(new InetSocketAddress(port));
    }
    
}
