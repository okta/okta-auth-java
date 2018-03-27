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
package com.okta.authn.sdk.client;

import com.okta.authn.sdk.AuthenticationException;
import com.okta.authn.sdk.AuthenticationStateHandler;
import com.okta.authn.sdk.resource.ActivateFactorRequest;
import com.okta.authn.sdk.resource.AuthenticationRequest;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.authn.sdk.resource.ChangePasswordRequest;
import com.okta.authn.sdk.resource.Factor;
import com.okta.authn.sdk.resource.FactorEnrollRequest;
import com.okta.authn.sdk.resource.RecoverPasswordRequest;
import com.okta.authn.sdk.resource.RecoveryQuestionAnswerRequest;
import com.okta.authn.sdk.resource.UnlockAccountRequest;
import com.okta.authn.sdk.resource.VerifyFactorRequest;
import com.okta.authn.sdk.resource.VerifyRecoveryRequest;
import com.okta.sdk.ds.DataStore;
import com.okta.sdk.resource.user.factor.FactorProfile;
import com.okta.sdk.resource.user.factor.FactorProvider;
import com.okta.sdk.resource.user.factor.FactorType;

import javax.annotation.Generated;

/**
 * https://developer.okta.com/docs/api/resources/authn.html
 */
public interface AuthenticationClient extends DataStore {

    // /api/v1/authn
    //
    AuthenticationResponse authenticate(String username, char[] password, String relayState, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    AuthenticationResponse authenticate(AuthenticationRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    // /api/v1/authn/credentials/change_password

    /**
     *
     * @param oldPassword User’s current password that is expired or about to expire
     * @param newPassword New password for user
     * @param stateToken state token for current transaction
     * @param stateHandler
     * @return
     * @throws AuthenticationException
     */
    AuthenticationResponse changePassword(char[] oldPassword, char[] newPassword, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;
    AuthenticationResponse changePassword(ChangePasswordRequest changePasswordRequest, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    // /api/v1/authn/credentials/reset_password

    /**
     *
     * @param newPassword User’s new password
     * @param stateToken state token for current transaction
     * @param stateHandler
     * @return
     * @throws AuthenticationException
     */
    AuthenticationResponse resetPassword(char[] newPassword, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;


    // /api/v1/authn/factors/{factorId}/verify

    AuthenticationResponse challengeFactor(Factor factor, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    // /api/v1/authn/factors

    AuthenticationResponse enrollFactor(FactorType factorType, FactorProvider factorProvider, FactorProfile factorProfile, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    AuthenticationResponse enrollFactor(FactorEnrollRequest factorEnrollRequest, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    // /api/v1/authn/factors/{factorId}/lifecycle/activate
    AuthenticationResponse activateFactor(String factorId, ActivateFactorRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    AuthenticationResponse verifyFactor(String factorId, VerifyFactorRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    //  /api/v1/authn/recovery/password
    AuthenticationResponse recoverPassword(String username, FactorType factorType, String relayState, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    AuthenticationResponse recoverPassword(RecoverPasswordRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException;


    // /api/v1/authn/recovery/unlock

    /**
     *
     * @param username User’s non-qualified short-name (dade.murphy) or unique fully-qualified login (dade.murphy@example.com)
     * @param factorType Recovery factor to use for primary authentication
     * @param relayState Optional state value that is persisted for the lifetime of the recovery transaction
     * @param stateHandler
     * @return
     * @throws AuthenticationException
     */
    AuthenticationResponse unlockAccount(String username, FactorType factorType, String relayState, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    AuthenticationResponse unlockAccount(UnlockAccountRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    // TODO: do not pass href here
    AuthenticationResponse verifyUnlockAccount(String href, VerifyRecoveryRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    AuthenticationResponse answerRecoveryQuestion(String answer, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    AuthenticationResponse answerRecoveryQuestion(RecoveryQuestionAnswerRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException;
}