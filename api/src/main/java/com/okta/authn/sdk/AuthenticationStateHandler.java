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

public interface AuthenticationStateHandler {

    void handleUnauthenticated(AuthenticationResponse typedUnauthenticatedResponse);

    void handlePasswordWarning(AuthenticationResponse passwordWarning);

    void handlePasswordExpired(AuthenticationResponse passwordExpired);

    void handleRecovery(AuthenticationResponse recovery);

    void handleRecoveryChallenge(AuthenticationResponse recoveryChallenge);

    void handlePasswordReset(AuthenticationResponse passwordReset);

    void handleLockedOut(AuthenticationResponse lockedOut);

    void handleMfaRequired(AuthenticationResponse mfaRequiredResponse);

    void handleMfaEnroll(AuthenticationResponse mfaEnroll);

    void handleMfaEnrollActivate(AuthenticationResponse mfaEnrollActivate);

    void handleMfaChallenge(AuthenticationResponse mfaChallengeResponse);

    void handleSuccess(AuthenticationResponse successResponse);

    void handleUnknown(AuthenticationResponse typedUnknownResponse);
}