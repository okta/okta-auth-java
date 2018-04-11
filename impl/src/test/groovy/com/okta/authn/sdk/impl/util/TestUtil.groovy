/*
 * Copyright 2018 Okta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.okta.authn.sdk.impl.util

import com.okta.sdk.impl.ds.DefaultResourceFactory
import com.okta.sdk.impl.ds.InternalDataStore
import com.okta.sdk.impl.resource.AbstractResource
import com.okta.sdk.resource.Resource
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.testng.Assert
import org.testng.TestException

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class TestUtil {

    static def expectException = { Class<? extends Throwable> catchMe, Closure callMe ->
        try {
            callMe.call()
            Assert.fail("Expected ${catchMe.getName()} to be thrown.")
        } catch(e) {
            if (!e.class.isAssignableFrom(catchMe)) {
                throw e
            }
        }
    }

    static InternalDataStore createMockDataStore() {
        InternalDataStore dataStore = mock(InternalDataStore)
        when(dataStore.getResource(Mockito.nullable(String), Mockito.any(Class.class)))
                .then(new Answer<Object>() {
            @Override
            Object answer(InvocationOnMock invocation) throws Throwable {
                def clazz = (Class)invocation.arguments[1]
                return  toResource(clazz, dataStore)

            }
        })

        when(dataStore.instantiate(Mockito.any(Class.class), Mockito.any(Map)))
                .then(new Answer<Object>() {
            @Override
            Object answer(InvocationOnMock invocation) throws Throwable {
                def clazz = (Class)invocation.arguments[0]
                Map data = invocation.arguments[1]
                return toResource(clazz, dataStore, data)
            }
        })

        return dataStore
    }

        static AbstractResource toResource(Class<Resource> clazz, InternalDataStore dataStore, Map data = Collections.emptyMap(), boolean fallback = true) {

        def resourceFactory = new DefaultResourceFactory(dataStore)
        try {
            return (AbstractResource) resourceFactory.instantiate(clazz, data)
        } catch (IllegalStateException e) {
            if (fallback) {
                return (AbstractResource) resourceFactory.instantiate(clazz)
            } else {
                throw new TestException(e)
            }
        }
    }
}