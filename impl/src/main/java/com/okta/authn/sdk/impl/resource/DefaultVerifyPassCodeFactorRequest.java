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

import com.okta.authn.sdk.resource.VerifyPassCodeFactorRequest;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.BooleanProperty;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultVerifyPassCodeFactorRequest extends DefaultVerifyFactorRequest implements VerifyPassCodeFactorRequest {

    private static final StringProperty PASS_CODE_PROPERTY = new StringProperty("passCode");

    private static final BooleanProperty REMEMBER_DEVICE_PROPERTY = new BooleanProperty("rememberDevice");

    public DefaultVerifyPassCodeFactorRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultVerifyPassCodeFactorRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public String getPassCode() {
        return getString(PASS_CODE_PROPERTY);
    }

    @Override
    public VerifyPassCodeFactorRequest setPassCode(String passCode) {
        setProperty(PASS_CODE_PROPERTY, passCode);
        return this;
    }

    @Override
    public Boolean getRememberDevice() {
        return getNullableBoolean(REMEMBER_DEVICE_PROPERTY);
    }

    @Override
    public VerifyPassCodeFactorRequest setRememberDevice(Boolean rememberDevice) {
        setProperty(REMEMBER_DEVICE_PROPERTY, rememberDevice);
        return this;
    }
}
