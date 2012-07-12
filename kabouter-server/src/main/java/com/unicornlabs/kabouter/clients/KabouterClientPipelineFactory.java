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

import com.unicornlabs.kabouter.clients.messaging.ClientMessage;
import com.unicornlabs.kabouter.net.JSONDecoder;
import com.unicornlabs.kabouter.net.JSONEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

/**
 * KabouterClientPipelineFactory.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: Pipeline Factory to handle requests from Kabouter Clients
 */
public class KabouterClientPipelineFactory implements
        ChannelPipelineFactory {

    private static final Logger LOGGER = Logger.getLogger(KabouterClientPipelineFactory.class.getName());

    static {
        LOGGER.setLevel(Level.ALL);
    }

    /**
     * Constructs the pipeline for handling client messaging
     *
     * @return the pipeline
     * @throws Exception
     */
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline myPipeline = Channels.pipeline();
        
        //Attach frame delimiter and string en/decoders
        myPipeline.addLast("framedelimiter", new DelimiterBasedFrameDecoder(
                8192, Delimiters.lineDelimiter()));

        myPipeline.addLast("stringdecoder", new StringDecoder());
        myPipeline.addLast("stringencoder", new StringEncoder());

        //Attach JSON en/decoders
        myPipeline.addLast("jsondecoder", new JSONDecoder<ClientMessage>(ClientMessage.class));
        myPipeline.addLast("jsonencoder", new JSONEncoder());

        //Attach a Kabouter Client Handler at the end of the stream
        myPipeline.addLast("handler", new KabouterClientHandler());

        return myPipeline;
    }
}
