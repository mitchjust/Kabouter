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

import com.unicornlabs.kabouter.util.JSONUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

/**
 * JSONDecoder.java
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description:
 * Upstream JSON Decoder for specified message class
 */

public class JSONDecoder<T> extends OneToOneDecoder {

    private static final Logger LOGGER = Logger.getLogger(JSONDecoder.class.getName());
    
    static{
        LOGGER.setLevel(Level.ALL);
    }
    
    /**
     * The decoded message class
     */
    private Class<T> messageClass;

    /**
     * Sets the message class
     * @param messageClass the message class
     */
    public JSONDecoder(Class<T> messageClass) {
        this.messageClass = messageClass;
    }

    /**
     * Decodes from a JSON String to a message object
     * @param chc the ChannelHandlerContext
     * @param chnl the Channel
     * @param o the JSON String
     * @return the decoded message object
     * @throws Exception 
     */
    @Override
    protected Object decode(ChannelHandlerContext chc, Channel chnl, Object o) throws Exception {
        if(!(o instanceof String)) {
            //Ignore it if we can't decode it
            return o;
        }
        
        String jsonString = (String) o;
        
        return JSONUtils.FromJSON(jsonString, messageClass);
    }

}
