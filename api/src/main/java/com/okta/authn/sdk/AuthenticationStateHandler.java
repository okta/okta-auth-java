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

import javax.validation.constraints.NotNull;

/**
 * Each method of {@code AuthenticationStateHandler} corresponds to a state of Okta's Authentication state machine. Each
 * method in @{@link com.okta.authn.sdk.client.AuthenticationClient AuthenticationClient} may return a different result
 * depending on the current state of the user (password locked, etc) and the current configuration of your Okta
 * organization.
 * <p>
 * <h1>Implementation Notes:</h1>
 * Your Okta organization MAY not be configured to enter all of these states. At minimum you should implement:
 * <ul>
 *     <li>{@code handleUnknown}</li>
 *     <li>{@code handleSuccess}</li>
 *     <li>{@code handlePasswordExpired}</li>
 *     <li>{@code handlePasswordWarning} (unless the {@code warnBeforePasswordExpired} flag was set when calling {@link com.okta.authn.sdk.client.AuthenticationClient#authenticate(com.okta.authn.sdk.resource.AuthenticationRequest, AuthenticationStateHandler)}</li>
 * </ul>
 * You must implement {@code handleLockedOut}, if self-service unlock is enabled for your Okta organization.
 * If you want to support password recovery flows you must implement:
 * <ul>
 *     <li>{@code handleRecovery}</li>
 *     <li>{@code handleRecoveryChallenge}</li>
 *     <li>{@code handlePasswordReset}</li>
 * </ul>
 *
 * For multi-factor flows you must implement:
 * <ul>
 *     <li>{@code handleMfaRequired}</li>
 *     <li>{@code handleMfaEnroll}</li>
 *     <li>{@code handleMfaEnrollActivate}</li>
 *     <li>{@code handleMfaChallenge}</li>
 * </ul>
 *
 * @since 0.1.0
 */
public interface AuthenticationStateHandler {

    void handleUnauthenticated(@NotNull AuthenticationResponse unauthenticatedResponse);

    void handlePasswordWarning(@NotNull AuthenticationResponse passwordWarning);

    void handlePasswordExpired(@NotNull AuthenticationResponse passwordExpired);

    void handleRecovery(@NotNull AuthenticationResponse recovery);

    void handleRecoveryChallenge(@NotNull AuthenticationResponse recoveryChallenge);

    void handlePasswordReset(@NotNull AuthenticationResponse passwordReset);

    void handleLockedOut(@NotNull AuthenticationResponse lockedOut);

    void handleMfaRequired(@NotNull AuthenticationResponse mfaRequiredResponse);

    void handleMfaEnroll(@NotNull AuthenticationResponse mfaEnroll);

    void handleMfaEnrollActivate(@NotNull AuthenticationResponse mfaEnrollActivate);

    void handleMfaChallenge(@NotNull AuthenticationResponse mfaChallengeResponse);

    void handleSuccess(@NotNull AuthenticationResponse successResponse);

    void handleUnknown(@NotNull AuthenticationResponse unknownResponse);
}