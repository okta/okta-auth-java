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

import com.okta.authn.sdk.resource.AuthenticationResponse;

/**
 * Adapter implementation of {@link AuthenticationStateHandler} that calls {@link #handleUnknown(AuthenticationResponse)} for each method.
 *
 * @since 0.1.0
 */
public abstract class AuthenticationStateHandlerAdapter implements AuthenticationStateHandler {

    @Override
    public void handleUnauthenticated(AuthenticationResponse unauthenticatedResponse) {
        this.handleUnknown(unauthenticatedResponse);
    }

    @Override
    public void handleSuccess(AuthenticationResponse successResponse) {
        this.handleUnknown(successResponse);
    }

    @Override
    public void handlePasswordWarning(AuthenticationResponse passwordWarning) {
        this.handleUnknown(passwordWarning);
    }

    @Override
    public void handlePasswordExpired(AuthenticationResponse passwordExpired) {
        this.handleUnknown(passwordExpired);
    }

    @Override
    public void handleRecovery(AuthenticationResponse recovery) {
        this.handleUnknown(recovery);
    }

    @Override
    public void handleRecoveryChallenge(AuthenticationResponse recoveryChallenge) {
        this.handleUnknown(recoveryChallenge);
    }

    @Override
    public void handlePasswordReset(AuthenticationResponse passwordReset) {
        this.handleUnknown(passwordReset);
    }

    @Override
    public void handleLockedOut(AuthenticationResponse lockedOut) {
        this.handleUnknown(lockedOut);
    }

    @Override
    public void handleMfaRequired(AuthenticationResponse mfaRequiredResponse) {
        this.handleUnknown(mfaRequiredResponse);
    }

    @Override
    public void handleMfaEnroll(AuthenticationResponse mfaEnroll) {
        this.handleUnknown(mfaEnroll);
    }

    @Override
    public void handleMfaEnrollActivate(AuthenticationResponse mfaEnrollActivate) {
        this.handleUnknown(mfaEnrollActivate);
    }

    @Override
    public void handleMfaChallenge(AuthenticationResponse mfaChallengeResponse) {
        this.handleUnknown(mfaChallengeResponse);
    }
}