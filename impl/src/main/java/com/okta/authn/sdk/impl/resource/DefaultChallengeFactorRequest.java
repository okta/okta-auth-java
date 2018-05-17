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

import com.okta.authn.sdk.resource.ChallengeFactorRequest;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultChallengeFactorRequest extends AbstractResource implements ChallengeFactorRequest {

    private static final StringProperty STATE_TOKEN_PROPERTY = new StringProperty("stateToken");

    public DefaultChallengeFactorRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultChallengeFactorRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(
            STATE_TOKEN_PROPERTY
        );
    }

    @Override
    public String getStateToken() {
        return getString(STATE_TOKEN_PROPERTY);
    }

    @Override
    public ChallengeFactorRequest setStateToken(String stateToken) {
        setProperty(STATE_TOKEN_PROPERTY, stateToken);
        return this;
    }
}