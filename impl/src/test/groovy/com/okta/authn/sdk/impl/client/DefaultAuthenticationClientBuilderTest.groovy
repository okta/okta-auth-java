/*
 * Copyright 2014 Stormpath, Inc.
 * Modifications Copyright 2018 Okta, Inc.
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
package com.okta.authn.sdk.impl.client

import com.okta.authn.sdk.client.AuthenticationClients
import com.okta.authn.sdk.impl.test.RestoreEnvironmentVariables
import com.okta.authn.sdk.impl.test.RestoreSystemProperties
import com.okta.sdk.client.AuthenticationScheme
import com.okta.sdk.impl.io.DefaultResourceFactory
import com.okta.sdk.impl.io.Resource
import com.okta.sdk.impl.io.ResourceFactory
import com.okta.sdk.impl.util.BaseUrlResolver
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.testng.annotations.Listeners
import org.testng.annotations.Test

import static org.mockito.ArgumentMatchers.anyString
import static org.mockito.Mockito.*
import static org.testng.Assert.assertEquals
import static org.testng.Assert.assertTrue

@Listeners([RestoreSystemProperties, RestoreEnvironmentVariables])
class DefaultAuthenticationClientBuilderTest {

    /**
     * This method MUST be called from each test in order to work with with the Listeners defined above.
     * If this method is invoked with an @BeforeMethod annotation the Listener will be invoked before and after this
     * method as well.
     */
    void clearOktaEnvAndSysProps() {
        System.clearProperty("okta.client.orgUrl")
        RestoreEnvironmentVariables.setEnvironmentVariable("OKTA_CLIENT_ORGURL", null)
    }

    @Test
    void testBuilder() {
        assertTrue(AuthenticationClients.builder() instanceof DefaultAuthenticationClientBuilder)
    }

    @Test
    void testConfigureBaseProperties() {
        clearOktaEnvAndSysProps()
        def builder = new DefaultAuthenticationClientBuilder(noDefaultYamlResourceFactory())
        DefaultAuthenticationClientBuilder clientBuilder = (DefaultAuthenticationClientBuilder) builder
        assertEquals clientBuilder.clientConfiguration.baseUrl, "https://api.okta.com/v42"
        assertEquals clientBuilder.clientConfiguration.connectionTimeout, 10
        assertEquals clientBuilder.clientConfiguration.authenticationScheme, AuthenticationScheme.NONE
    }

    @Test
    void testConfigureProxy() {
        clearOktaEnvAndSysProps()
        def builder = AuthenticationClients.builder()
        DefaultAuthenticationClientBuilder clientBuilder = (DefaultAuthenticationClientBuilder) builder
        assertEquals clientBuilder.clientConfiguration.proxyHost, "proxyyaml" // from yaml
        assertEquals clientBuilder.clientConfiguration.proxyPort, 9009 // from yaml
        assertEquals clientBuilder.clientConfiguration.proxyUsername, "fooyaml" // from yaml
        assertEquals clientBuilder.clientConfiguration.proxyPassword, "bar" // from properties
    }

    @Test
    void testConfigureBaseUrlResolver(){
        BaseUrlResolver baseUrlResolver = new BaseUrlResolver() {
            @Override
            String getBaseUrl() {
                return "test"
            }
        }

        def testClient = new DefaultAuthenticationClientBuilder().setBaseUrlResolver(baseUrlResolver).build()

        assertEquals(testClient.dataStore.baseUrlResolver.getBaseUrl(), "test")
    }

    @Test
    void testDefaultBaseUrlResolver(){
        clearOktaEnvAndSysProps()
        def client = new DefaultAuthenticationClientBuilder(noDefaultYamlResourceFactory()).build()
        assertEquals(client.dataStore.baseUrlResolver.getBaseUrl(), "https://api.okta.com/v42")
    }

    static ResourceFactory noDefaultYamlResourceFactory() {
        def resourceFactory = spy(new DefaultResourceFactory())
        doAnswer(new Answer<Resource>() {
            @Override
            Resource answer(InvocationOnMock invocation) throws Throwable {
                if (invocation.arguments[0].toString().endsWith("/.okta/okta.yaml")) {
                    return mock(Resource)
                }
                else {
                    return invocation.callRealMethod()
                }
            }
        })
        .when(resourceFactory).createResource(anyString())

        return resourceFactory
    }
}
