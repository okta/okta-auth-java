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
import com.okta.authn.sdk.doc.ApiReference;
import com.okta.authn.sdk.resource.ActivateFactorRequest;
import com.okta.authn.sdk.resource.AuthenticationRequest;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.authn.sdk.resource.ChangePasswordRequest;
import com.okta.authn.sdk.resource.FactorEnrollRequest;
import com.okta.authn.sdk.resource.RecoverPasswordRequest;
import com.okta.authn.sdk.resource.RecoveryQuestionAnswerRequest;
import com.okta.authn.sdk.resource.UnlockAccountRequest;
import com.okta.authn.sdk.resource.VerifyFactorRequest;
import com.okta.authn.sdk.resource.VerifyRecoveryRequest;
import com.okta.sdk.ds.DataStore;
import com.okta.sdk.resource.Resource;
import com.okta.sdk.resource.user.factor.FactorProfile;
import com.okta.sdk.resource.user.factor.FactorProvider;
import com.okta.sdk.resource.user.factor.FactorType;

/**
 * The Okta Authentication API provides operations to authenticate users, perform multi-factor enrollment and verification,
 * recover forgotten passwords, and unlock accounts. It can be used as a standalone API to provide the identity layer on top
 * of your existing application, or it can be integrated with the Okta Sessions API to obtain an Okta session cookie and
 * access apps within Okta.
 * <p>
 * The API is targeted for developers who want to build their own end-to-end login experience to replace the built-in Okta
 * login experience and addresses the following key scenarios:
 * <ul>
 *   <li>Primary authentication allows you to verify username and password credentials for a user.</li>
 *   <li>Multifactor authentication (MFA) strengthens the security of password-based authentication by requiring additional verification of another factor such as a temporary one-time password or an SMS passcode. The Authentication API supports user enrollment with MFA factors enabled by the administrator, as well as MFA challenges based on your Okta Sign-On Policy.</li>
 *   <li>Recovery allows users to securely reset their password if they’ve forgotten it, or unlock their account if it has been locked out due to excessive failed login attempts. This functionality is subject to the security policy set by the administrator.</li>
 * </ul>
 *
 * Create a client using the {@link AuthenticationClients} builder.
 * <pre>
 * Client client = AuthenticationClients.builder().build();
 * </pre>
 *
 * <strong>NOTE:</strong> We recommend you evaluate using an <a href="https://developer.okta.com/authentication-guide/">OIDC/OAuth 2.0</a> flow before considering this API
 * @see <a href="https://developer.okta.com/docs/api/resources/authn.html">Okta Authentication API documentation</a>
 * @since 0.1.0
 */
public interface AuthenticationClient {

    /**
    * Returns the internal {@link DataStore} of the client.  It is typically not necessary to invoke this method as
    * the Client implements the {@link DataStore} API and will delegate to this instance automatically.
    *
    * @return the client's internal {@link DataStore}.
    */
    DataStore getDataStore();

    /**
     * Delegates to the internal {@code dataStore} instance. This is a convenience mechanism to eliminate the constant
     * need to call {@code client.getDataStore()} every time one needs to instantiate Resource.
     *
     * @param clazz the Resource class to instantiate.
     * @param <T>   the Resource sub-type
     * @return a new instance of the specified Resource.
     */
    <T extends Resource> T instantiate(Class<T> clazz);

    /**
     * Initiates a username and password login against Okta's Authentication API. A user should not be considered logged in
     * until both the response status is {code SUCCESS} and the sessionToken is non null.
     *
     * @param username User’s non-qualified short-name (e.g. dade.murphy) or unique fully-qualified login (e.g dade.murphy@example.com)
     * @param password User’s password credential
     * @param relayState Optional state value that is persisted for the lifetime of the authentication transaction
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws com.okta.authn.sdk.AuthenticationFailureException when username or password are invalid
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn", href = "https://developer.okta.com/docs/api/resources/authn.html#primary-authentication")
    AuthenticationResponse authenticate(String username, char[] password, String relayState, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Initiates a username and password login against Okta's Authentication API. A user should not be considered logged in
     * until both the response status is {code SUCCESS} and the sessionToken is non null.
     *
     * @param request a request object holds all attributes sent to the remote API.
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws com.okta.authn.sdk.AuthenticationFailureException when username or password are invalid
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn", href = "https://developer.okta.com/docs/api/resources/authn.html#primary-authentication")
    AuthenticationResponse authenticate(AuthenticationRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * This operation changes a user’s password by providing the existing password and the new password password for authentication transactions with either the PASSWORD_EXPIRED or PASSWORD_WARN state.
     *
     * @param oldPassword User’s current password that is expired or about to expire
     * @param newPassword New password for user
     * @param stateToken state token for current transaction
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws com.okta.authn.sdk.CredentialsException thrown if old password is invalid, or the new password fails to meet the
     * requirements of the password policy
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/credentials/change_password", href = "https://developer.okta.com/docs/api/resources/authn.html#change-password")
    AuthenticationResponse changePassword(char[] oldPassword, char[] newPassword, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * This operation changes a user’s password by providing the existing password and the new password password for authentication transactions with either the PASSWORD_EXPIRED or PASSWORD_WARN state.
     *
     * @param changePasswordRequest a request object holds all attributes sent to the remote API.
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws com.okta.authn.sdk.CredentialsException thrown if old password is invalid, or the new password fails to meet the
     * requirements of the password policy
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/credentials/change_password", href = "https://developer.okta.com/docs/api/resources/authn.html#change-password")
    AuthenticationResponse changePassword(ChangePasswordRequest changePasswordRequest, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Resets a user’s password to complete a recovery transaction with a PASSWORD_RESET state.
     *
     * @param newPassword User’s new password
     * @param stateToken state token for current transaction
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws com.okta.authn.sdk.CredentialsException thrown if old password is invalid, or the new password fails to meet the
     * requirements of the password policy
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/credentials/reset_password", href = "https://developer.okta.com/docs/api/resources/authn.html#reset-password")
    AuthenticationResponse resetPassword(char[] newPassword, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Resets a user’s password to complete a recovery transaction with a PASSWORD_RESET state.
     *
     * @param changePasswordRequest a request object holds all attributes sent to the remote API
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws com.okta.authn.sdk.CredentialsException thrown if old password is invalid, or the new password fails to meet the
     * requirements of the password policy
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/credentials/reset_password", href = "https://developer.okta.com/docs/api/resources/authn.html#reset-password")
    AuthenticationResponse resetPassword(ChangePasswordRequest changePasswordRequest, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Enrolls a user with a factor assigned by their MFA Policy.
     *
     * @see <a href="https://developer.okta.com/docs/api/resources/factors#supported-factors-for-providers">supported factor profiles</a>
     * @param factorType type of factor
     * @param factorProvider factor provider
     * @param factorProfile profile of a supported factor
     * @param stateToken state token for current transaction
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/factors", href = "https://developer.okta.com/docs/api/resources/authn.html#enroll-factor")
    AuthenticationResponse enrollFactor(FactorType factorType, FactorProvider factorProvider, FactorProfile factorProfile, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Enrolls a user with a factor assigned by their MFA Policy.
     *
     * @see <a href="https://developer.okta.com/docs/api/resources/factors#supported-factors-for-providers">supported factor profiles</a>
     * @param factorEnrollRequest a request object holds all attributes sent to the remote API.
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/factors", href = "https://developer.okta.com/docs/api/resources/authn.html#enroll-factor")
    AuthenticationResponse enrollFactor(FactorEnrollRequest factorEnrollRequest, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Starts a new password recovery transaction for a given user and issues a recovery token that can be used to reset a user’s password.
     * @param username User’s non-qualified short-name (e.g. dade.murphy) or unique fully-qualified login (dade.murphy@example.com)
     * @param factorType Recovery factor to use for primary authentication
     * @param relayState Optional state value that is persisted for the lifetime of the recovery transaction
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/recovery/password", href = "https://developer.okta.com/docs/api/resources/authn.html#recovery-operations")
    AuthenticationResponse recoverPassword(String username, FactorType factorType, String relayState, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Starts a new password recovery transaction for a given user and issues a recovery token that can be used to reset a user’s password.
     * @param request a request object holds all attributes sent to the remote API.
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/recovery/password", href = "https://developer.okta.com/docs/api/resources/authn.html#recovery-operations")
    AuthenticationResponse recoverPassword(RecoverPasswordRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Starts a new unlock recovery transaction for a given user and issues a recovery token that can be used to unlock a user’s account.
     *
     * @param username User’s non-qualified short-name (dade.murphy) or unique fully-qualified login (dade.murphy@example.com)
     * @param factorType Recovery factor to use for primary authentication
     * @param relayState Optional state value that is persisted for the lifetime of the recovery transaction
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/recovery/unlock", href = "https://developer.okta.com/docs/api/resources/authn.html#unlock-account")
    AuthenticationResponse unlockAccount(String username, FactorType factorType, String relayState, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Starts a new unlock recovery transaction for a given user and issues a recovery token that can be used to unlock a user’s account.
     *
     * @param request a request object holds all attributes sent to the remote API.
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/recovery/unlock", href = "https://developer.okta.com/docs/api/resources/authn.html#unlock-account")
    AuthenticationResponse unlockAccount(UnlockAccountRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Answers the user’s recovery question to ensure only the end user redeemed the recovery token for recovery transaction
     * with a RECOVERY status.
     *
     * @param answer answer to user’s recovery question
     * @param stateToken state token for current transaction
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws com.okta.authn.sdk.InvalidRecoveryAnswerException thrown when the answer is invalid
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/recovery/answer", href = "https://developer.okta.com/docs/api/resources/authn.html#answer-recovery-question")
    AuthenticationResponse answerRecoveryQuestion(String answer, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Answers the user’s recovery question to ensure only the end user redeemed the recovery token for recovery transaction
     * with a RECOVERY status.
     *
     * @param request a request object holds all attributes sent to the remote API.
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws com.okta.authn.sdk.InvalidRecoveryAnswerException thrown when the answer is invalid
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/recovery/answer", href = "https://developer.okta.com/docs/api/resources/authn.html#answer-recovery-question")
    AuthenticationResponse answerRecoveryQuestion(RecoveryQuestionAnswerRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Moves the current transaction state back to the previous state.
     *
     * @param stateToken state token for current transaction
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/previous", href = "https://developer.okta.com/docs/api/resources/authn.html#previous-transaction-state")
    AuthenticationResponse previous(String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Skip the current transaction state and advance to the next state.
     *
     * @param stateToken state token for current transaction
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/skip", href = "https://developer.okta.com/docs/api/resources/authn.html#skip-transaction-state")
    AuthenticationResponse skip(String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Cancels the current transaction and revokes the state token.
     *
     * @param stateToken state token for current transaction
     * @return An authentication response
     */
    @ApiReference(path = "/api/v1/authn/cancel", href = "https://developer.okta.com/docs/api/resources/authn.html#cancel-transaction")
    AuthenticationResponse cancel(String stateToken);

    /**
     * The sms, call and token:software:totp factor types require activation to complete the enrollment process.
     *
     * @param factorId id of factor returned from enrollment
     * @param request the request object containing the required attributes to fulfill the activation
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/factors/{factorId}/lifecycle/activate", href = "https://developer.okta.com/docs/api/resources/authn.html#activate-factor")
    AuthenticationResponse activateFactor(String factorId, ActivateFactorRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Verifies an enrolled factor for an authentication transaction with the MFA_REQUIRED or MFA_CHALLENGE state
     *
     * @param factorId id of factor returned from enrollment
     * @param request the request object containing the required attributes to fulfill the verification
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/factors/{factorId}/verify", href = "https://developer.okta.com/docs/api/resources/authn.html#verify-factor")
    AuthenticationResponse verifyFactor(String factorId, VerifyFactorRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Verifies the state of a factor. Some factors (Push, Duo, etc) depend on a user action, this method can be used to poll the state of
     * the a factor and transition to the next state when completed.
     *
     * @param factorId id of factor returned from enrollment
     * @param stateToken state token for current transaction
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/factors/{factorId}/verify", href = "https://developer.okta.com/docs/api/resources/authn#poll-for-push-factor-activation")
    AuthenticationResponse verifyFactor(String factorId, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Requests a challenge factor be sent to the user via the corresponding {code}factorId{code}.
     *
     * @param factorId id of factor returned from enrollment
     * @param stateToken state token for current transaction
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/factors/{factorId}/verify", href = "https://developer.okta.com/docs/api/resources/authn.html#verify-sms-factor")
    AuthenticationResponse challengeFactor(String factorId, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Verifies a recovery challenge sent to the user for primary authentication for a recovery transaction with RECOVERY_CHALLENGE status.
     *
     * @param factorType type of factor
     * @param request the request object containing the required attributes to fulfill this challenge
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/recovery/factors/{factorType}/verify", href = "https://developer.okta.com/docs/api/resources/authn.html#verify-recovery-factor")
    AuthenticationResponse verifyUnlockAccount(FactorType factorType, VerifyRecoveryRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Resend an activation factor challenge to a user. Factors that require the challenge sent to the user (push, call, sms, etc) may need
     * to be resent to ensure delivery.
     *
     * @param factorId id of factor returned from enrollment
     * @param stateToken state token for current transaction
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/factors/{factorId}/lifecycle/resend", href = "https://developer.okta.com/docs/api/resources/authn.html")
    AuthenticationResponse resendActivateFactor(String factorId, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Resend a factor verification challenge to a user. Factors that require the challenge sent to the user (push, call, sms, etc) may need
     * to be resent to ensure delivery.
     *
     * @param factorId id of factor returned from enrollment
     * @param stateToken state token for current transaction
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/factors/{factorId}/verify/resend", href = "https://developer.okta.com/docs/api/resources/authn.html")
    AuthenticationResponse resendVerifyFactor(String factorId, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Returns the state of factor's activation. Some factors (Push, Duo, etc) depend on a user action, this method can
     * be used to poll the state of the a factor's activation and transition to the next state when completed.
     *
     * @param stateToken state token for current transaction
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/factors/{factorId}/lifecycle/activate/poll", href = "https://developer.okta.com/docs/api/resources/authn#poll-for-push-factor-activation")
    AuthenticationResponse getFactorActivationStatus(String factorId, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;

    /**
     * Validates a recovery token that was distributed to the end user to continue the recovery transaction.
     *
     * @param recoveryToken Recovery token that was distributed to the end user via out-of-band mechanism such as email
     * @param stateHandler State handler that handles the resulting status change corresponding to the Okta authentication state machine
     * @return An authentication response
     * @throws AuthenticationException any other authentication related error
     */
    @ApiReference(path = "/api/v1/authn/recovery/token", href = "https://developer.okta.com/docs/api/resources/authn.html#verify-recovery-token")
    AuthenticationResponse verifyRecoveryToken(String recoveryToken, AuthenticationStateHandler stateHandler) throws AuthenticationException;
}