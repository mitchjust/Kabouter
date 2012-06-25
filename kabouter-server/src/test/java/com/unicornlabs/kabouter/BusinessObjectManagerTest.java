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
package com.unicornlabs.kabouter;

import junit.framework.TestCase;

/**
 *
 * @author Mitch
 */
public class BusinessObjectManagerTest extends TestCase {
    
    public BusinessObjectManagerTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of class BusinessObjectManager.
     */
    public void testBusinessObjectManager() {
        BusinessObjectManager theBOM = BusinessObjectManager.getInstance();
        
        assertNotNull(theBOM);
        
        Object testObject = new Object();
        
        BusinessObjectManager.registerBusinessObject(testObject.toString(), testObject);
        
        Object testObject2 = BusinessObjectManager.getBusinessObject(testObject.toString());
        
        assertEquals(testObject2, testObject);
    }
}
