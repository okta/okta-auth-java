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
package com.okta.authn.sdk;

import com.okta.authn.sdk.resource.AuthenticationRequest;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.authn.sdk.resource.ChangePasswordRequest;
import com.okta.authn.sdk.resource.Factor;
import com.okta.sdk.ds.DataStore;

public interface AuthenticationClient extends DataStore {

    void authenticate(String username, char[] password, StateHandler stateHandler) throws AuthenticationException;

    void authenticate(AuthenticationRequest request, StateHandler stateHandler) throws AuthenticationException;

    void changePassword(char[] oldPassword, char[] newPassword, String stateToken, StateHandler stateHandler) throws AuthenticationException;

    void changePassword(ChangePasswordRequest changePasswordRequest, StateHandler stateHandler) throws AuthenticationException;

    void challengeFactor(Factor factor, String stateToken, StateHandler stateHandler) throws AuthenticationException;

    void verifyFactor(Factor factor, AuthenticationRequest request, StateHandler stateHandler) throws AuthenticationException;

    default AuthenticationRequest fromResult(AuthenticationResponse result) {
        return instantiate(AuthenticationRequest.class)
            .setStateToken(result.getStateToken())
            .setRelayState(result.getRelayState());
    }
}