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

import com.okta.authn.sdk.resource.AuthNResult;

public abstract class StateHandlerAdapter implements StateHandler {

    private AuthNResult result;

    @Override
    public void handleUnauthenticated(AuthNResult unauthenticatedResponse) {
        this.handleUnknown(unauthenticatedResponse);
    }

    @Override
    public void handleSuccess(AuthNResult successResponse) {
        this.handleUnknown(successResponse);
    }

    @Override
    public void handlePasswordWarning(AuthNResult passwordWarning) {
        this.handleUnknown(passwordWarning);
    }

    @Override
    public void handlePasswordExpired(AuthNResult passwordExpired) {
        this.handleUnknown(passwordExpired);
    }

    @Override
    public void handleRecovery(AuthNResult recovery) {
        this.handleUnknown(recovery);
    }

    @Override
    public void handleRecoveryChallenge(AuthNResult recoveryChallenge) {
        this.handleUnknown(recoveryChallenge);
    }

    @Override
    public void handlePasswordReset(AuthNResult passwordReset) {
        this.handleUnknown(passwordReset);
    }

    @Override
    public void handleLockedOut(AuthNResult lockedOut) {
        this.handleUnknown(lockedOut);
    }

    @Override
    public void handleMfaRequired(AuthNResult mfaRequiredResponse) {
        this.handleUnknown(mfaRequiredResponse);
    }

    @Override
    public void handleMfaEnroll(AuthNResult mfaEnroll) {
        this.handleUnknown(mfaEnroll);
    }

    @Override
    public void handleMfaEnrollActivate(AuthNResult mfaEnrollActivate) {
        this.handleUnknown(mfaEnrollActivate);
    }

    @Override
    public void handleMfaChallenge(AuthNResult mfaChallengeResponse) {
        this.handleUnknown(mfaChallengeResponse);
    }

    // force handling of unknown ?

    public AuthNResult getResult() {
        return result;
    }

    protected void setResult(AuthNResult result) {
        this.result = result;
    }
}