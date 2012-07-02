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
import com.unicornlabs.kabouter.devices.events.DeviceEvent;
import com.unicornlabs.kabouter.historian.Historian;
import com.unicornlabs.kabouter.historian.data_objects.Device;
import com.unicornlabs.kabouter.historian.data_objects.Powerlog;
import com.unicornlabs.kabouter.historian.data_objects.PowerlogId;
import com.unicornlabs.kabouter.util.JSONUtils;
import java.util.Date;
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
    private Historian theHistorian;
    
    public KabouterDeviceHandler() {
        theDeviceManager = (DeviceManager) BusinessObjectManager.getBusinessObject(DeviceManager.class.getName());
        theHistorian = (Historian) BusinessObjectManager.getBusinessObject(Historian.class.getName());
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
        DeviceInfo deviceInfo = (DeviceInfo) ctx.getAttachment();
        Device device = deviceInfo.theDevice;
        LOGGER.log(Level.SEVERE, "Exception Caught for Device {0}:\n{1}", new Object[]{device.getId(), e.getCause().getMessage()});
        deviceInfo.isConnected = false;
        DeviceEvent newDeviceEvent = new DeviceEvent(theDeviceManager, DeviceEvent.DEVICE_DISCONNECTION_EVENT, deviceInfo);
        theDeviceManager.fireDeviceEvent(newDeviceEvent);
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
            //Attach the channel
            deviceInfo.tcpChannel = ctx.getChannel();
            
            //Attach the device to the channel context
            ctx.setAttachment(deviceInfo);
        }
        
        //handle message stuff
        
        if(message.messageType.contentEquals(DeviceServerMessage.IO_STATE_CHANGE)) {
            Integer[] newIOStates = JSONUtils.FromJSON(message.data, Integer[].class);
            deviceInfo.ioStates = newIOStates;
            
            DeviceEvent deviceIOEvent = new DeviceEvent(theDeviceManager, DeviceEvent.IO_CHANGE_EVENT, deviceInfo);
            
            theDeviceManager.fireDeviceEvent(deviceIOEvent);
        }
        else if(message.messageType.contentEquals(DeviceServerMessage.POWER_LOG)) {
            Double power = JSONUtils.FromJSON(message.data, Double.class);
            
            Powerlog newPowerlog = new Powerlog(new PowerlogId(new Date(), theDevice.getId()), (double)power);
            
            theHistorian.savePowerlog(newPowerlog);
            
            DeviceEvent devicePowerEvent = new DeviceEvent(theDeviceManager, DeviceEvent.POWER_LOG_EVENT, newPowerlog);
            
            theDeviceManager.fireDeviceEvent(devicePowerEvent);
        }
    }
    
    

}
