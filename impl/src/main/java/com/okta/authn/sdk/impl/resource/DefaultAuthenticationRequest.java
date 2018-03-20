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
import com.okta.sdk.impl.resource.BooleanProperty;
import com.okta.sdk.impl.resource.CharacterArrayProperty;
import com.okta.sdk.impl.resource.MapProperty;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.ResourceReference;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultAuthenticationRequest extends AbstractResource implements AuthenticationRequest {

    private static final StringProperty STATE_TOKEN_PROPERTY = new StringProperty("stateToken");

    private static final StringProperty RELAY_STATE_PROPERTY = new StringProperty("relayState");

    private static final StringProperty USERNAME_PROPERTY = new StringProperty("username");

    private static final CharacterArrayProperty PASSWORD_PROPERTY = new CharacterArrayProperty("password");

    private static final StringProperty FACTOR_ID_PROPERTY = new StringProperty("factorId");

    private static final StringProperty PASS_CODE_PROPERTY = new StringProperty("passCode");

    private static final StringProperty NEXT_PASS_CODE_PROPERTY = new StringProperty("nextPassCode");

    private static final StringProperty ANSWER_PROPERTY = new StringProperty("answer");

    private static final StringProperty CLIENT_DATA_PROPERTY = new StringProperty("clientData");

    private static final StringProperty AUTHENTICATIOR_DATA_PROPERTY = new StringProperty("authenticatorData");

    private static final StringProperty SIGNATURE_DATA_PROPERTY = new StringProperty("signatureData");

    private static final StringProperty RECOVERY_TOKEN_PROPERTY = new StringProperty("recoveryToken");

    private static final StringProperty FACTOR_TYPE_PROPERTY = new StringProperty("factorType");

    private static final StringProperty TOKEN_PROPERTY = new StringProperty("token");

    private static final StringProperty AUDIENCE_PROPERTY = new StringProperty("audience");

    private static final BooleanProperty REMEMBER_DEVICE_PROPERTY = new BooleanProperty("rememberDevice");

    private static final BooleanProperty AUTO_PUSH_PROPERTY = new BooleanProperty("rememberDevice");

    private static final ResourceReference<Options> OPTIONS_PROPERTY = new ResourceReference<>("options", Options.class, true);

    private static final MapProperty CONTEXT_PROPERTY = new MapProperty("context");

    public DefaultAuthenticationRequest(InternalDataStore dataStore) {
        super(dataStore);
//        setResourceHref("/api/v1/authn");
    }

    public DefaultAuthenticationRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
        // TODO: maybe just always return this url from the getter ?
//        setResourceHref("/api/v1/authn");
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(
            STATE_TOKEN_PROPERTY,
            RELAY_STATE_PROPERTY,
            USERNAME_PROPERTY,
            PASSWORD_PROPERTY,
            FACTOR_ID_PROPERTY,
            PASS_CODE_PROPERTY,
            NEXT_PASS_CODE_PROPERTY,
            ANSWER_PROPERTY,
            CLIENT_DATA_PROPERTY,
            AUTHENTICATIOR_DATA_PROPERTY,
            SIGNATURE_DATA_PROPERTY,
            RECOVERY_TOKEN_PROPERTY,
            FACTOR_TYPE_PROPERTY,
            TOKEN_PROPERTY,
            AUDIENCE_PROPERTY,
            REMEMBER_DEVICE_PROPERTY,
            AUTO_PUSH_PROPERTY,
            OPTIONS_PROPERTY,
            CONTEXT_PROPERTY
        );
    }

    @Override
    public String getStateToken() {
        return getString(STATE_TOKEN_PROPERTY);
    }

    @Override
    public AuthenticationRequest setStateToken(String stateToken) {
        setProperty(STATE_TOKEN_PROPERTY, stateToken);
        return this;
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
    public String getFactorId() {
        return getString(FACTOR_ID_PROPERTY);
    }

    @Override
    public AuthenticationRequest setFactorId(String factorId) {
        setProperty(FACTOR_ID_PROPERTY, factorId);
        return this;
    }

    @Override
    public String getPassCode() {
        return getString(PASS_CODE_PROPERTY);
    }

    @Override
    public AuthenticationRequest setPassCode(String passCode) {
        setProperty(PASS_CODE_PROPERTY, passCode);
        return this;
    }

    @Override
    public String getNextPassCode() {
        return getString(NEXT_PASS_CODE_PROPERTY);
    }

    @Override
    public AuthenticationRequest setNextPassCode(String nextPassCode) {
        return this;
    }

    @Override
    public String getAnswer() {
        return getString(ANSWER_PROPERTY);
    }

    @Override
    public AuthenticationRequest setAnswer(String answer) {
        setProperty(ANSWER_PROPERTY, answer);
        return this;
    }

    @Override
    public String getClientData() {
        return getString(CLIENT_DATA_PROPERTY);
    }

    @Override
    public AuthenticationRequest setClientData(String clientData) {
        setProperty(CLIENT_DATA_PROPERTY, clientData);
        return this;
    }

    @Override
    public String getAuthenticatorData() {
        return getString(AUTHENTICATIOR_DATA_PROPERTY);
    }

    @Override
    public AuthenticationRequest setAuthenticatorData(String authenticatorData) {
        setProperty(AUTHENTICATIOR_DATA_PROPERTY, authenticatorData);
        return this;
    }

    @Override
    public String getSignatureData() {
        return getString(SIGNATURE_DATA_PROPERTY);
    }

    @Override
    public AuthenticationRequest setSignatureData(String signatureData) {
        setProperty(SIGNATURE_DATA_PROPERTY, signatureData);
        return this;
    }

    @Override
    public String getRecoveryToken() {
        return getString(RECOVERY_TOKEN_PROPERTY);
    }

    @Override
    public AuthenticationRequest setRecoveryToken(String recoveryToken) {
        setProperty(RECOVERY_TOKEN_PROPERTY, recoveryToken);
        return this;
    }

    @Override
    public String getFactorType() {
        return getString(FACTOR_TYPE_PROPERTY);
    }

    @Override
    public AuthenticationRequest setFactorType(String factorType) {
        setProperty(FACTOR_TYPE_PROPERTY, factorType);
        return this;
    }

    @Override
    public String getToken() {
        return getString(TOKEN_PROPERTY);
    }

    @Override
    public AuthenticationRequest setToken(String token) {
        setProperty(TOKEN_PROPERTY, token);
        return this;
    }

    @Override
    public Boolean isRememberDevice() {
        return getBoolean(REMEMBER_DEVICE_PROPERTY);
    }

    @Override
    public AuthenticationRequest setRememberDevice(Boolean rememberDevice) {
        setProperty(REMEMBER_DEVICE_PROPERTY, rememberDevice);
        return this;
    }

    @Override
    public Boolean isAutoPush() {
        return getBoolean(AUTO_PUSH_PROPERTY);
    }

    @Override
    public AuthenticationRequest setAutoPush(Boolean autoPush) {
        setProperty(AUTO_PUSH_PROPERTY, autoPush);
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