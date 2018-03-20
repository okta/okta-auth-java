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
package com.okta.authn.sdk.resource;

import com.okta.sdk.resource.Resource;

import java.util.Map;

public interface AuthenticationRequest extends Resource {

    String getStateToken();

    AuthenticationRequest setStateToken(String stateToken);

    String getRelayState();

    AuthenticationRequest setRelayState(String relayState);

    String getUsername();

    AuthenticationRequest setUsername(String username);

    AuthenticationRequest setPassword(char[] password);

    String getFactorId();

    AuthenticationRequest setFactorId(String factorId);

    String getPassCode();

    AuthenticationRequest setPassCode(String passCode);

    String getNextPassCode();

    AuthenticationRequest setNextPassCode(String nextPassCode);

    String getAnswer();

    AuthenticationRequest setAnswer(String answer);

    String getClientData();

    AuthenticationRequest setClientData(String clientData);

    String getAuthenticatorData();

    AuthenticationRequest setAuthenticatorData(String authenticatorData);

    String getSignatureData();

    AuthenticationRequest setSignatureData(String signatureData);

    String getRecoveryToken();

    AuthenticationRequest setRecoveryToken(String recoveryToken);

    String getFactorType();

    AuthenticationRequest setFactorType(String factorType);

    String getToken();

    AuthenticationRequest setToken(String token);

    Boolean isRememberDevice();

    AuthenticationRequest setRememberDevice(Boolean rememberDevice);

    Boolean isAutoPush();

    AuthenticationRequest setAutoPush(Boolean autoPush);

    String getAudience();

    AuthenticationRequest setAudience(String audience);

    Map<String, Object> getContext();

    AuthenticationRequest setContext(Map<String, Object> context);

    Options getOptions();

    AuthenticationRequest setOptions(Options options);
}