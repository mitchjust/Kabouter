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
import com.unicornlabs.kabouter.devices.DeviceStatus;
import com.unicornlabs.kabouter.devices.DeviceManager;
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

    /**
     * Bind to business objects
     */
    public KabouterClientHandler() {
        theDeviceManager = (DeviceManager) BusinessObjectManager.getBusinessObject(DeviceManager.class.getName());
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
        ctx.getChannel().write(theDeviceManager.getDeviceInfos());
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelDisconnected(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        LOGGER.log(Level.SEVERE, "Exception Caught: {0}", e.toString());
    }

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        super.handleUpstream(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (!(e.getMessage() instanceof ClientServerMessage)) {
            LOGGER.log(Level.SEVERE, "Unknown Message Received: \n{0}", e.getMessage());
        }

        ClientServerMessage message = (ClientServerMessage) e.getMessage();

        if (message.messageType.contentEquals(ClientServerMessage.DEVICE_INFO_REQUEST)) {
            DeviceStatus di = theDeviceManager.getDeviceInfo(message.deviceId);

            if (di == null) {
                LOGGER.log(Level.SEVERE, "Device Requested unknown device id: {0}", message.deviceId);
                ctx.getChannel().write("Unknown Device ID!");
            } else {
                ctx.getChannel().write(theDeviceManager.getDeviceInfo(message.deviceId));
            }
        } else if (message.messageType.contentEquals(ClientServerMessage.DEVICE_CONTROL_REQUEST)) {
            //TODO Handle io change
        } else {
            LOGGER.log(Level.SEVERE, "Unknown Message Type: \n{0}", e.getMessage());
        }
    }
}
