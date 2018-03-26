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

import com.okta.authn.sdk.resource.AuthenticationRequest;
import com.okta.authn.sdk.resource.Options;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.CharacterArrayProperty;
import com.okta.sdk.impl.resource.MapProperty;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.ResourceReference;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultAuthenticationRequest extends AbstractResource implements AuthenticationRequest {

    private static final StringProperty RELAY_STATE_PROPERTY = new StringProperty("relayState");

    private static final StringProperty USERNAME_PROPERTY = new StringProperty("username");

    private static final CharacterArrayProperty PASSWORD_PROPERTY = new CharacterArrayProperty("password");

    private static final StringProperty AUDIENCE_PROPERTY = new StringProperty("audience");

    private static final ResourceReference<Options> OPTIONS_PROPERTY = new ResourceReference<>("options", Options.class, true);

    private static final MapProperty CONTEXT_PROPERTY = new MapProperty("context");

    public DefaultAuthenticationRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultAuthenticationRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(
            RELAY_STATE_PROPERTY,
            USERNAME_PROPERTY,
            PASSWORD_PROPERTY,
            AUDIENCE_PROPERTY,
            OPTIONS_PROPERTY,
            CONTEXT_PROPERTY
        );
    }

    @Override
    public String getRelayState() {
        return getString(RELAY_STATE_PROPERTY);
    }

    @Override
    public AuthenticationRequest setRelayState(String relayState) {
        setProperty(RELAY_STATE_PROPERTY, relayState);
        return this;
    }

    @Override
    public String getUsername() {
        return getString(USERNAME_PROPERTY);
    }

    @Override
    public AuthenticationRequest setUsername(String username) {
        setProperty(USERNAME_PROPERTY, username);
        return this;
    }

    @Override
    public AuthenticationRequest setPassword(char[] password) {
        setProperty(PASSWORD_PROPERTY, password);
        return this;
    }

    @Override
    public String getAudience() {
        return getString(AUDIENCE_PROPERTY);
    }

    @Override
    public AuthenticationRequest setAudience(String audience) {
        setProperty(AUDIENCE_PROPERTY, audience);
        return this;
    }

    @Override
    public Map<String, Object> getContext() {
        return getMap(CONTEXT_PROPERTY);
    }

    @Override
    public AuthenticationRequest setContext(Map<String, Object> context) {
        setProperty(CONTEXT_PROPERTY, context);
        return this;
    }

    @Override
    public Options getOptions() {
        return getResourceProperty(OPTIONS_PROPERTY);
    }

    @Override
    public AuthenticationRequest setOptions(Options options) {
        setProperty(OPTIONS_PROPERTY, options);
        return this;
    }
}