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
package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.VerifyFactorRequest;
import com.okta.authn.sdk.resource.VerifyU2fFactorRequest;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultVerifyU2fFactorRequest extends DefaultVerifyFactorRequest implements VerifyU2fFactorRequest {


    private static final StringProperty SIGNATURE_DATA_PROPERTY = new StringProperty("signatureData");

    private static final StringProperty CLIENT_DATA_PROPERTY = new StringProperty("clientData");

    public DefaultVerifyU2fFactorRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultVerifyU2fFactorRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public String getClientData() {
        return getString(CLIENT_DATA_PROPERTY);
    }

    @Override
    public VerifyU2fFactorRequest setClientData(String clientData) {
        setProperty(CLIENT_DATA_PROPERTY, clientData);
        return this;
    }

    @Override
    public String getSignatureData() {
        return getString(SIGNATURE_DATA_PROPERTY);
    }

    @Override
    public VerifyFactorRequest setSignatureData(String signatureData) {
        setProperty(SIGNATURE_DATA_PROPERTY, signatureData);
        return this;
    }
}
