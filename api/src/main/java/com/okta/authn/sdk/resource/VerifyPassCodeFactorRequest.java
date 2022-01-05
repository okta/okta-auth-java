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

public interface VerifyPassCodeFactorRequest extends VerifyFactorRequest {

    String getPassCode();

    VerifyPassCodeFactorRequest setPassCode(String passCode);

    /**
     * @deprecated 'rememberDevice' property is no longer a body param but an url/query param.
     * See https://developer.okta.com/docs/reference/api/authn/#verify-security-question-factor.
     * Use {@link com.okta.authn.sdk.http.RequestContext#getQueryParams()} to get query parameters.
     */
    @Deprecated
    Boolean getRememberDevice();

    /**
     * @deprecated 'rememberDevice' property is no longer a body param but an url/query param.
     * See https://developer.okta.com/docs/reference/api/authn/#verify-security-question-factor.
     * Use {@link com.okta.authn.sdk.http.RequestContext#addQuery(String, String)} to set a query parameter key-value.
     */
    @Deprecated
    VerifyPassCodeFactorRequest setRememberDevice(Boolean rememberDevice);
}