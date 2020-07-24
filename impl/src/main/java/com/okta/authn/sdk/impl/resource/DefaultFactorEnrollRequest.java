/*
 * Copyright 2018-Present Okta, Inc.
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

import com.okta.authn.sdk.resource.FactorEnrollRequest;
import com.okta.authn.sdk.resource.FactorProfile;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.EnumProperty;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.ResourceReference;
import com.okta.sdk.impl.resource.StringProperty;
import com.okta.authn.sdk.resource.FactorProvider;
import com.okta.authn.sdk.resource.FactorType;

import java.util.Map;

public class DefaultFactorEnrollRequest extends AbstractResource implements FactorEnrollRequest {

    private static final StringProperty STATE_TOKEN_PROPERTY = new StringProperty("stateToken");
    private static final EnumProperty<FactorType> FACTOR_TYPE_PROPERTY = new EnumProperty<>("factorType", FactorType.class);
    private static final ResourceReference<FactorProfile> FACTOR_PROFILE_PROPERTY = new ResourceReference<>("profile", FactorProfile.class, true);
    private static final EnumProperty<FactorProvider> PROVIDER_PROPERTY = new EnumProperty<>("provider", FactorProvider.class);

    public DefaultFactorEnrollRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultFactorEnrollRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(STATE_TOKEN_PROPERTY, FACTOR_TYPE_PROPERTY, FACTOR_PROFILE_PROPERTY, PROVIDER_PROPERTY);
    }

    @Override
    public String getStateToken() {
        return getString(STATE_TOKEN_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setStateToken(String stateToken) {
        setProperty(STATE_TOKEN_PROPERTY, stateToken);
        return this;
    }

    @Override
    public FactorType getFactorType() {
        return getEnumProperty(FACTOR_TYPE_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setFactorType(FactorType factorType) {
        setProperty(FACTOR_TYPE_PROPERTY, factorType);
        return this;
    }

    @Override
    public FactorProvider getProvider() {
        return  getEnumProperty(PROVIDER_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setProvider(FactorProvider provider) {
        setProperty(PROVIDER_PROPERTY, provider);
        return this;
    }

    @Override
    public FactorProfile getFactorProfile() {
        return getResourceProperty(FACTOR_PROFILE_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setFactorProfile(FactorProfile factorProfile) {
        setProperty(FACTOR_PROFILE_PROPERTY, factorProfile);
        return this;
    }
}
