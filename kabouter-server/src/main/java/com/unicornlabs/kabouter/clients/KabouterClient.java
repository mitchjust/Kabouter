/*
 * Copyright 2012 Mitch.
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
package com.unicornlabs.kabouter.clients;

import org.jboss.netty.channel.Channel;

/**
 *
 * @author Mitch
 */
public class KabouterClient {
    public Channel clientChannel;
    public String deviceOfInterest;

    public KabouterClient(Channel clientChannel, String deviceOfInterest) {
        this.clientChannel = clientChannel;
        this.deviceOfInterest = deviceOfInterest;
    }

    public KabouterClient(Channel clientChannel) {
        this(clientChannel,null);
    }
    
    
}
