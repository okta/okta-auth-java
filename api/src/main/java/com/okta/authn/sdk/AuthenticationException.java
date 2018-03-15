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

import com.okta.sdk.error.Error;
import com.okta.sdk.error.ErrorCause;
import com.okta.sdk.resource.ResourceException;

import java.util.List;
import java.util.Map;

public class AuthenticationException extends Exception implements Error {

    private final Error error;

    public AuthenticationException(Error error) {
        super(buildExceptionMessage(error));
        this.error = error;
    }

    public AuthenticationException(ResourceException e) {
        super(buildExceptionMessage(e.getError()), e);
        this.error = e;
    }

    private static String buildExceptionMessage(Error error) {
        return error.getMessage();
    }

    @Override
    public int getStatus() {
        return error.getStatus();
    }

    /**
     * Get the Okta Error Code, <a href="https://developer.okta.com/reference/error_codes/">click here</a> for the
     * list of Okta error codes.
     *
     * @return the code of the error
     */
    @Override
    public String getCode() {
        return error.getCode();
    }


    @Override
    public String getId() {
        return error.getId();
    }

    @Override
    public List<ErrorCause> getCauses() {
        return error.getCauses();
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return error.getHeaders();
    }
}
