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

import com.unicornlabs.kabouter.BusinessObjectManager;
import com.unicornlabs.kabouter.clients.messaging.ClientDeviceObject;
import com.unicornlabs.kabouter.clients.messaging.ClientMessage;
import com.unicornlabs.kabouter.clients.messaging.IOUpdate;
import com.unicornlabs.kabouter.devices.DeviceManager;
import com.unicornlabs.kabouter.devices.DeviceStatus;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.netty.channel.*;

/**
 * KabouterClientHandler.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: Handles Client communications
 */
public class KabouterClientHandler extends SimpleChannelUpstreamHandler {

    private static final Logger LOGGER = Logger.getLogger(KabouterClientHandler.class.getName());

    static {
        LOGGER.setLevel(Level.ALL);
    }
    /**
     * The device manager reference
     */
    private DeviceManager theDeviceManager;
    private ClientManager theClientManager;

    /**
     * Bind to business objects
     */
    public KabouterClientHandler() {
        theDeviceManager = (DeviceManager) BusinessObjectManager.getBusinessObject(DeviceManager.class.getName());
        theClientManager = (ClientManager) BusinessObjectManager.getBusinessObject(ClientManager.class.getName());
    }

    /**
     * Override channel connected method to automatically send current device
     * info
     *
     * @param ctx the ChannelHandlerContext
     * @param e the related ChannelStateEvent
     * @throws Exception
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        LOGGER.info("New Client Connected: " + ctx.getChannel().getRemoteAddress());
        KabouterClient newClient = theClientManager.clientConnected(ctx.getChannel());
        ctx.setAttachment(newClient);
        DeviceStatus[] deviceStatuses = theDeviceManager.getDeviceStatuses();
        ArrayList<ClientDeviceObject> myClientDeviceObjects = new ArrayList<ClientDeviceObject>();
        
        for(DeviceStatus d : deviceStatuses) {
            if(d.theDevice.getIodirections().contains("output")) {
                ClientDeviceObject newClientDeviceObject = new ClientDeviceObject();
                newClientDeviceObject.deviceId = d.theDevice.getId();
                
                ArrayList<String> ioNames = new ArrayList<String>();
                ArrayList<String> ioTypes = new ArrayList<String>();
                for(int i=0;i<d.theDevice.getIonames().size();i++) {
                    if(d.theDevice.getIodirections().get(i).contentEquals("output")) {
                        ioNames.add(d.theDevice.getIonames().get(i));
                        ioTypes.add(d.theDevice.getIotypes().get(i));
                    }
                }
                newClientDeviceObject.deviceIoNames = ioNames;
                newClientDeviceObject.deviceIoTypes = ioTypes;
                
                myClientDeviceObjects.add(newClientDeviceObject);
                
                System.out.println("newClientDeviceObject.deviceId = " + newClientDeviceObject.deviceId);
            }
        }
        
        ctx.getChannel().write(myClientDeviceObjects);
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        LOGGER.log(Level.INFO, "Client Disconnected: {0}", ctx.getChannel().getRemoteAddress());
        KabouterClient client = (KabouterClient) ctx.getAttachment();
        if (client != null) {
            theClientManager.clientDisconnected(client);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        LOGGER.log(Level.SEVERE, "Exception Caught: {0}", e.toString());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (e.getMessage() instanceof IOUpdate) {
            IOUpdate theIOUpdate = (IOUpdate) e.getMessage();
            DeviceStatus deviceStatus = theDeviceManager.getDeviceStatus(theIOUpdate.deviceId);
            if(deviceStatus != null && deviceStatus.isConnected) {
                deviceStatus.tcpChannel.write(theIOUpdate.ioName + ":" + theIOUpdate.value);
            }
        }
        else {
            System.out.println("chuck a shit");
        }
    }
}
