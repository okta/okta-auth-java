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

public interface AuthNRequest extends Resource {

    String getStateToken();

    AuthNRequest setStateToken(String stateToken);

    String getRelayState();

    AuthNRequest setRelayState(String relayState);

    String getUsername();

    AuthNRequest setUsername(String username);

    AuthNRequest setPassword(char[] password);

    String getFactorId();

    AuthNRequest setFactorId(String factorId);

    String getPassCode();

    AuthNRequest setPassCode(String passCode);

    String getNextPassCode();

    AuthNRequest setNextPassCode(String nextPassCode);

    String getAnswer();

    AuthNRequest setAnswer(String answer);

    String getClientData();

    AuthNRequest setClientData(String clientData);

    String getAuthenticatorData();

    AuthNRequest setAuthenticatorData(String authenticatorData);

    String getSignatureData();

    AuthNRequest setSignatureData(String signatureData);

    String getRecoveryToken();

    AuthNRequest setRecoveryToken(String recoveryToken);

    String getFactorType();

    AuthNRequest setFactorType(String factorType);

    String getToken();

    AuthNRequest setToken(String token);

    Boolean isRememberDevice();

    AuthNRequest setRememberDevice(Boolean rememberDevice);

    Boolean isAutoPush();

    AuthNRequest setAutoPush(Boolean autoPush);

    String getAudience();

    AuthNRequest setAudience(String audience);

    Map<String, Object> getContext();

    AuthNRequest setContext(Map<String, Object> context);

    Options getOptions();

    AuthNRequest setOptions(Options options);
}