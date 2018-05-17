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

import com.okta.authn.sdk.resource.VerifyRecoveryRequest;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultVerifyRecoveryRequest extends AbstractResource implements VerifyRecoveryRequest {

    private static final StringProperty PASS_CODE_PROPERTY = new StringProperty("passCode");

    private static final StringProperty STATE_TOKEN_PROPERTY = new StringProperty("stateToken");

    public DefaultVerifyRecoveryRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultVerifyRecoveryRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(PASS_CODE_PROPERTY, STATE_TOKEN_PROPERTY);
    }

    @Override
    public String getStateToken() {
        return getString(STATE_TOKEN_PROPERTY);
    }

    @Override
    public VerifyRecoveryRequest setStateToken(String stateToken) {
        setProperty(STATE_TOKEN_PROPERTY, stateToken);
        return this;
    }

    @Override
    public String getPassCode() {
        return getString(PASS_CODE_PROPERTY);
    }

    @Override
    public VerifyRecoveryRequest setPassCode(String passCode) {
        setProperty(PASS_CODE_PROPERTY, passCode);
        return this;
    }
}
