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

/**
 * Opt-in features for the authentication transaction.
 *
 * @since 0.1.0
 */
public interface Options extends Resource {

    /**
     * Returns true if the user should transition back to {@code MFA_ENROLL} state after successful factor enrollment when
     * additional optional factors are available for enrollment. (not required)
     * @return true if user can enroll in multiple factors.
     */
    Boolean isMultiOptionalFactorEnroll();

    /**
     * Sets whether user should transaction back to MFA_ENROLL state after successful factor enrollment when
     * additional optional factors are available for enrollment. (not required)
     * @return the current object for method chaining.
     */
    Options setMultiOptionalFactorEnroll(Boolean multiOptionalFactorEnroll);

    /**
     * Returns true if user should transition to {@code PASSWORD_WARN} state before {@code SUCCESS} if the user’s password is
     * about to expire and within their password policy warn period. (not required)
     * @return true if user should warned about password expiration.
     */
    Boolean isWarnBeforePasswordExpired();

    /**
     * Sets whether if user should transition to {@code PASSWORD_WARN} state before {@code SUCCESS} if the user’s password is
     * about to expire and within their password policy warn period. (not required)
     * @return the current object for method chaining.
     */
    Options setWarnBeforePasswordExpired(Boolean warnBeforePasswordExpired);
}