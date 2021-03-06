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

import com.okta.authn.sdk.resource.RecoverPasswordRequest;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.EnumProperty;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;
import com.okta.authn.sdk.resource.FactorType;

import java.util.Map;

public class DefaultRecoverPasswordRequest extends AbstractResource implements RecoverPasswordRequest {

    private static final StringProperty RELAY_STATE_PROPERTY = new StringProperty("relayState");

    private static final StringProperty USERNAME_PROPERTY = new StringProperty("username");

    private static final EnumProperty<FactorType> FACTOR_TYPE_PROPERTY = new EnumProperty<>("factorType", FactorType.class);

    public DefaultRecoverPasswordRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultRecoverPasswordRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(
            RELAY_STATE_PROPERTY,
            USERNAME_PROPERTY,
            FACTOR_TYPE_PROPERTY
        );
    }

    @Override
    public String getRelayState() {
        return getString(RELAY_STATE_PROPERTY);
    }

    @Override
    public RecoverPasswordRequest setRelayState(String relayState) {
        setProperty(RELAY_STATE_PROPERTY, relayState);
        return this;
    }

    @Override
    public String getUsername() {
        return getString(USERNAME_PROPERTY);
    }

    @Override
    public RecoverPasswordRequest setUsername(String username) {
        setProperty(USERNAME_PROPERTY, username);
        return this;
    }

    @Override
    public FactorType getFactorType() {
        return getEnumProperty(FACTOR_TYPE_PROPERTY);
    }

    @Override
    public RecoverPasswordRequest setFactorType(FactorType factorType) {
        setProperty(FACTOR_TYPE_PROPERTY, factorType);
        return this;
    }
}