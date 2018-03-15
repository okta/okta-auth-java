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

public interface StateHandler {

    void handleUnauthenticated(AuthNResult typedUnauthenticatedResponse);

    void handlePasswordWarning(AuthNResult passwordWarning);

    void handlePasswordExpired(AuthNResult passwordExpired);

    void handleRecovery(AuthNResult recovery);

    void handleRecoveryChallenge(AuthNResult recoveryChallenge);

    void handlePasswordReset(AuthNResult passwordReset);

    void handleLockedOut(AuthNResult lockedOut);

    void handleMfaRequired(AuthNResult mfaRequiredResponse);

    void handleMfaEnroll(AuthNResult mfaEnroll);

    void handleMfaEnrollActivate(AuthNResult mfaEnrollActivate);

    void handleMfaChallenge(AuthNResult mfaChallengeResponse);

    void handleSuccess(AuthNResult successResponse);

    void handleUnknown(AuthNResult typedUnknownResponse);
}