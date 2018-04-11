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

import com.okta.authn.sdk.resource.VerifyPushFactorRequest;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.BooleanProperty;
import com.okta.sdk.impl.resource.Property;

import java.util.Map;

public class DefaultVerifyPushFactorRequest extends DefaultVerifyFactorRequest implements VerifyPushFactorRequest {

    private static final BooleanProperty AUTO_PUSH_PROPERTY = new BooleanProperty("autoPush");

    private static final BooleanProperty REMEMBER_DEVICE_PROPERTY = new BooleanProperty("rememberDevice");

    public DefaultVerifyPushFactorRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultVerifyPushFactorRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(super.getPropertyDescriptors(), AUTO_PUSH_PROPERTY, REMEMBER_DEVICE_PROPERTY);
    }

    @Override
    public Boolean getRememberDevice() {
        return getNullableBoolean(REMEMBER_DEVICE_PROPERTY);
    }

    @Override
    public VerifyPushFactorRequest setRememberDevice(Boolean rememberDevice) {
        setProperty(REMEMBER_DEVICE_PROPERTY, rememberDevice);
        return this;
    }

    @Override
    public Boolean getAutoPush() {
        return getNullableBoolean(AUTO_PUSH_PROPERTY);
    }

    @Override
    public VerifyPushFactorRequest setAutoPush(Boolean autoPush) {
        setProperty(AUTO_PUSH_PROPERTY, autoPush);
        return this;
    }
}
