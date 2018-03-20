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

/**
 * From: https://developer.okta.com/docs/api/resources/authn#transaction-state
 */
public enum AuthenticationStatus {

    /**
     * User tried to access protected resource (ex: an app) but user is not authenticated
     */
    UNAUTHENTICATED,

    /**
     * The user’s password was successfully validated but is about to expire and should be changed.
     */

    PASSWORD_WARN,
    /**
     * The user’s password was successfully validated but is expired.
     */

    PASSWORD_EXPIRED,
    /**
     * The user has requested a recovery token to reset their password or unlock their account.
     */
    RECOVERY,

    /**
     * The user must verify the factor-specific recovery challenge.
     */
    RECOVERY_CHALLENGE,
    /**
     * The user successfully answered their recovery question and must to set a new password.
     */
    PASSWORD_RESET,

    /**
     * The user account is locked; self-service unlock or admin unlock is required.
     */
    LOCKED_OUT,

    /**
     * The user must select and enroll an available factor for additional verification.
     */
    MFA_ENROLL,

    /**
     * The user must activate the factor to complete enrollment.
     */
    MFA_ENROLL_ACTIVATE,

    /**
     * The user must provide additional verification with a previously enrolled factor.
     */
    MFA_REQUIRED,

    /**
     * The user must verify the factor-specific challenge.
     */
    MFA_CHALLENGE,

    /**
     * The transaction has completed successfully.
     */
    SUCCESS,

    /**
     * Other state NOT directly supported by this implementation.
     */
    UNKNOWN
}
