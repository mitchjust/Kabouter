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
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * JSONEncoder.java
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description:
 * Downstream JSON Encoder
 */

public class JSONEncoder extends OneToOneEncoder{

    private static final Logger LOGGER = Logger.getLogger(JSONEncoder.class.getName());
    
    static{
        LOGGER.setLevel(Level.ALL);
    }

    /**
     * Encodes a message to a JSON String
     * @param chc the ChannelHandlerContext
     * @param chnl the Channel
     * @param o the message object
     * @return the JSON String
     * @throws Exception 
     */
    @Override
    protected Object encode(ChannelHandlerContext chc, Channel chnl, Object o) throws Exception {    
        String jsonString = JSONUtils.ToJSON(o);
        
        LOGGER.log(Level.INFO, "Writing JSON String:\n{0}", jsonString);
        
        return jsonString;
    }

}
