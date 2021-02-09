/*
 * Copyright 2021-Present Okta, Inc.
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

import com.okta.sdk.error.Error;
import com.okta.sdk.resource.ResourceException;

/**
 * Factor user locked exception. This exception
 * is thrown when a user gets locked out and status code {@code 403} is returned, related error messages are
 * contained within this exception.
 *
 * @since 2.0.1
 */
public class UserLockedException extends AuthenticationException {

    public static final String ERROR_CODE = "E0000069";

    public UserLockedException(Error error) {
        super(error);
    }

    public UserLockedException(ResourceException e) {
        super(e);
    }
}