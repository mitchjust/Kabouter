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
import com.unicornlabs.kabouter.devices.events.IOEvent;
import com.unicornlabs.kabouter.devices.messaging.DeviceServerMessage;
import com.unicornlabs.kabouter.historian.Historian;
import com.unicornlabs.kabouter.historian.data_objects.Device;
import com.unicornlabs.kabouter.historian.data_objects.Powerlog;
import com.unicornlabs.kabouter.historian.data_objects.PowerlogId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.netty.channel.*;

/**
 * KabouterDeviceHandler.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: Handler for Kabouter-enabled end devices
 */
public class KabouterDeviceHandler extends SimpleChannelHandler {

    private static final Logger LOGGER = Logger.getLogger(KabouterDeviceHandler.class.getName());

    static {
        LOGGER.setLevel(Level.ALL);
    }
    private DeviceManager theDeviceManager;
    private Historian theHistorian;

    public KabouterDeviceHandler() {
        theDeviceManager = (DeviceManager) BusinessObjectManager.getBusinessObject(DeviceManager.class.getName());
        theHistorian = (Historian) BusinessObjectManager.getBusinessObject(Historian.class.getName());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        DeviceStatus deviceStatus = (DeviceStatus) ctx.getAttachment();
        Device device = deviceStatus.theDevice;
        LOGGER.log(Level.SEVERE, "Exception Caught for Device {0}:\n{1}", new Object[]{device.getId(), e.getCause().getMessage()});
        deviceStatus.isConnected = false;
        DeviceEvent newDeviceEvent = new DeviceEvent(theDeviceManager, DeviceEvent.DEVICE_DISCONNECTION_EVENT, deviceStatus, null);
        theDeviceManager.fireDeviceEvent(newDeviceEvent);
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        DeviceStatus deviceStatus = (DeviceStatus) ctx.getAttachment();
        Device device = deviceStatus.theDevice;
        LOGGER.log(Level.INFO, "Device Connection Gracefully Closed: {0}", device.getId());
        deviceStatus.isConnected = false;
        DeviceEvent newDeviceEvent = new DeviceEvent(theDeviceManager, DeviceEvent.DEVICE_DISCONNECTION_EVENT, deviceStatus, null);
        theDeviceManager.fireDeviceEvent(newDeviceEvent);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        DeviceServerMessage message = (DeviceServerMessage) e.getMessage();
        DeviceStatus deviceStatus = (DeviceStatus) ctx.getAttachment();

        if (deviceStatus == null) {
            //First connection from a device

            if (!message.messageType.contentEquals(DeviceServerMessage.DEVICE_CONFIG)) {
                LOGGER.severe("Device did not send Configuration Message");
                return;
            }

            String[] split = ((String) message.data).split(":");

            if (split.length != 2) {
                LOGGER.log(Level.SEVERE, "Invalid Configuration String: {0}", message.data);
                ctx.getChannel().close();
                return;
            }

            deviceStatus = theDeviceManager.getDeviceStatus(split[0]);

            if (deviceStatus == null) {
                //Device has never connected before
                deviceStatus = theDeviceManager.insertNewDevice(split[0], split[1], ctx.getChannel().getRemoteAddress().toString());
                LOGGER.log(Level.INFO, "New Device Has Connected: {0}", deviceStatus.theDevice.getDisplayname());
            } else {
                LOGGER.log(Level.INFO, "Familiar Device Has Connected: {0}", deviceStatus.theDevice.getDisplayname());
            }

            //Set device as ready and attach comms channel
            deviceStatus.isConnected = true;
            deviceStatus.tcpChannel = ctx.getChannel();

            //Attach devicestatus to the channel context
            ctx.setAttachment(deviceStatus);

            LOGGER.log(Level.INFO, "Successfully configured Device: {0}", deviceStatus.theDevice.getId());

            //Fire device connection event
            DeviceEvent event = new DeviceEvent(theDeviceManager, DeviceEvent.DEVICE_CONNECTION_EVENT, deviceStatus, null);
            theDeviceManager.fireDeviceEvent(event);
        } else {
            //Device is already connected, handle possible messages

            if (message.messageType.contentEquals(DeviceServerMessage.IO_STATE_UPDATE)) {
            } else if (message.messageType.contentEquals(DeviceServerMessage.POWER_LOG)) {
                String data = (String) message.data;
                String[] split = data.split(":");
                if (split.length != 2) {
                    LOGGER.log(Level.SEVERE, "Unknown Message Received From: ", deviceStatus.theDevice.getDisplayname()
                            + "\nContents: " + data);
                } else {
                    Double powerValue = Double.parseDouble(split[0]);
                    Double tempValue = Double.parseDouble(split[1]);
                    Powerlog newPowerlog = new Powerlog(new PowerlogId(new Date(), deviceStatus.theDevice.getId()), powerValue);
                    theHistorian.savePowerlog(newPowerlog);
                    DeviceEvent devicePowerEvent = new DeviceEvent(theDeviceManager, DeviceEvent.POWER_LOG_EVENT, deviceStatus, newPowerlog);
                    theDeviceManager.fireDeviceEvent(devicePowerEvent);
                    
                    IOEvent tempIoEvent = new IOEvent("temp_in", tempValue);
                    DeviceEvent deviceTempEvent = new DeviceEvent(theDeviceManager, DeviceEvent.IO_CHANGE_EVENT, deviceStatus, tempIoEvent);
                    theDeviceManager.fireDeviceEvent(deviceTempEvent);
                }
            }

        }
    }
}
