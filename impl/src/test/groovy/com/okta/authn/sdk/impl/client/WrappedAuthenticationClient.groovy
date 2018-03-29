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

import com.okta.sdk.authc.credentials.ClientCredentials
import com.okta.sdk.cache.CacheManager
import com.okta.sdk.client.AuthenticationScheme
import com.okta.sdk.client.Proxy
import com.okta.sdk.impl.api.ClientCredentialsResolver
import com.okta.sdk.impl.ds.InternalDataStore
import com.okta.sdk.impl.http.Request
import com.okta.sdk.impl.http.RequestExecutor
import com.okta.sdk.impl.http.Response
import com.okta.sdk.impl.http.RestException
import com.okta.sdk.impl.http.authc.RequestAuthenticatorFactory
import com.okta.sdk.impl.util.BaseUrlResolver

class WrappedAuthenticationClient extends DefaultAuthenticationClient {

    InternalDataStore exposedDataStore
    DelegatingRequestExecutor delegatingRequestExecutor

    WrappedAuthenticationClient(BaseUrlResolver baseUrlResolver, Proxy proxy, CacheManager cacheManager, AuthenticationScheme authenticationScheme, RequestAuthenticatorFactory requestAuthenticatorFactory, int connectionTimeout) {
        super(baseUrlResolver, proxy, cacheManager, authenticationScheme, requestAuthenticatorFactory, connectionTimeout)
    }

    protected InternalDataStore createDataStore(RequestExecutor requestExecutor,
                                                BaseUrlResolver baseUrlResolver,
                                                ClientCredentialsResolver clientCredentialsResolver,
                                                CacheManager cacheManager) {

        exposedDataStore = super.createDataStore(requestExecutor, baseUrlResolver, clientCredentialsResolver, cacheManager)
        return exposedDataStore
    }

    protected RequestExecutor createRequestExecutor(ClientCredentials clientCredentials,
                                                    Proxy proxy,
                                                    AuthenticationScheme authenticationScheme,
                                                    RequestAuthenticatorFactory requestAuthenticatorFactory,
                                                    int connectionTimeout) {
        delegatingRequestExecutor = new DelegatingRequestExecutor()
        return delegatingRequestExecutor
    }

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
    Response executeRequest(Request request) throws RestException {
        return executor.executeRequest(request)
    }
}
