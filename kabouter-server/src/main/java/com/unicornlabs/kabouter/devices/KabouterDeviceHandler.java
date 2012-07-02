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

package com.unicornlabs.kabouter.devices;

import com.unicornlabs.kabouter.BusinessObjectManager;
import com.unicornlabs.kabouter.historian.data_objects.Device;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.netty.channel.*;

/**
 * KabouterDeviceHandler.java
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description:
 * Handler for Kabouter-enabled end devices
 */

public class KabouterDeviceHandler extends SimpleChannelHandler{

    private static final Logger LOGGER = Logger.getLogger(KabouterDeviceHandler.class.getName());
    
    static{
        LOGGER.setLevel(Level.ALL);
    }
    
    private DeviceManager theDeviceManager;
    
    public KabouterDeviceHandler() {
        theDeviceManager = (DeviceManager) BusinessObjectManager.getBusinessObject(DeviceManager.class.getName());
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelConnected(ctx, e);
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelDisconnected(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        super.exceptionCaught(ctx, e);
    }

    @Override
    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        super.handleDownstream(ctx, e);
    }

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        super.handleUpstream(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        DeviceServerMessage message = (DeviceServerMessage) e.getMessage();
        
        Device theDevice = message.device;
        
        LOGGER.log(Level.INFO, "Handling Device Message From {0}", theDevice.getId());
        
        DeviceInfo deviceInfo = theDeviceManager.getDeviceInfo(theDevice.getId());
        
        if(deviceInfo == null) {
            //First connection from a new device
            LOGGER.log(Level.INFO, "Unknown Device, Creating new configuration");
            deviceInfo = theDeviceManager.insertNewDevice(theDevice);
        }
        
        if(deviceInfo.isConnected == false) {
            //Set connected state to true
            deviceInfo.isConnected = true;
        }
        
        //handle message stuff
    }
    
    

}
