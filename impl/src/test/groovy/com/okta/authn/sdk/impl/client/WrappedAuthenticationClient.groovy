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
package com.okta.authn.sdk.impl.client

import com.okta.commons.http.HttpException
import com.okta.commons.http.Request
import com.okta.commons.http.RequestExecutor
import com.okta.commons.http.Response
import com.okta.commons.http.config.BaseUrlResolver
import com.okta.sdk.authc.credentials.ClientCredentials
import com.okta.sdk.cache.CacheManager
import com.okta.sdk.client.AuthenticationScheme
import com.okta.sdk.impl.api.ClientCredentialsResolver
import com.okta.sdk.impl.config.ClientConfiguration
import com.okta.sdk.impl.ds.InternalDataStore
import com.okta.sdk.impl.http.authc.RequestAuthenticatorFactory

class WrappedAuthenticationClient extends DefaultAuthenticationClient {

    InternalDataStore exposedDataStore
    DelegatingRequestExecutor delegatingRequestExecutor

    WrappedAuthenticationClient(ClientConfiguration clientConfiguration) {
        super(clientConfiguration)
    }

    protected InternalDataStore createDataStore(RequestExecutor requestExecutor,
                                                BaseUrlResolver baseUrlResolver,
                                                ClientCredentialsResolver clientCredentialsResolver,
                                                CacheManager cacheManager) {

        exposedDataStore = super.createDataStore(requestExecutor, baseUrlResolver, clientCredentialsResolver, cacheManager)
        return exposedDataStore
    }

    @Override
    protected RequestExecutor createRequestExecutor(ClientConfiguration clientConfiguration) {
        delegatingRequestExecutor = new DelegatingRequestExecutor()
        return delegatingRequestExecutor
    }

//    //@Override
//    protected RequestExecutor createRequestExecutor(ClientCredentials clientCredentials,
//                                                    Proxy proxy,
//                                                    AuthenticationScheme authenticationScheme,
//                                                    RequestAuthenticatorFactory requestAuthenticatorFactory,
//                                                    int connectionTimeout) {
//        delegatingRequestExecutor = new DelegatingRequestExecutor()
//        return delegatingRequestExecutor
//    }

    void setRequestExecutor(RequestExecutor executor) {
        delegatingRequestExecutor.executor = executor
    }

    RequestExecutor getRequestExecutor() {
        return delegatingRequestExecutor.executor
    }
}

class DelegatingRequestExecutor implements RequestExecutor {

    RequestExecutor executor = new StubRequestExecutor()

    @Override
    Response executeRequest(Request request) throws HttpException {
        return executor.executeRequest(request)
    }
}
