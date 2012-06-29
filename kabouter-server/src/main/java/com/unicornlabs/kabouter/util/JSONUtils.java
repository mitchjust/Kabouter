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
package com.unicornlabs.kabouter.util;

import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * GSONUtils.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: Easily Convert between JSON and Objects
 */
public class JSONUtils {

    private static final Logger LOGGER = Logger.getLogger(JSONUtils.class.getName());

    static {
        LOGGER.setLevel(Level.ALL);
    }

    /**
     * Returns the JSON string representing the object
     *
     * @param theObject object to be converted
     * @return the JSON String
     */
    public static String ToJSON(Object theObject) {
        Gson myGson = new Gson();
        String jsonString = myGson.toJson(theObject);
        return jsonString;
    }

    /**
     * Return the object parsed from the JSON String
     *
     * @param <T> The Class to return as
     * @param jsonString The JSON String representing the object
     * @param classOfT The Class to return as
     * @return The object
     */
    public static <T> T FromJSON(String jsonString, Class<T> classOfT) {
        Gson myGson = new Gson();
        T theObject = myGson.fromJson(jsonString, classOfT);
        return theObject;
    }
}
