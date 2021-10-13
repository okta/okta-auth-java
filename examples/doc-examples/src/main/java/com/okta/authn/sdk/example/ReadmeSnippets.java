/*
 * Copyright 2018-Present Okta, Inc.
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
package com.okta.authn.sdk.example;

import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.client.AuthenticationClients;
import com.okta.authn.sdk.http.Header;
import com.okta.authn.sdk.http.QueryParameter;
import com.okta.authn.sdk.http.RequestContext;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "PMD.AvoidUsingHardCodedIP"})
public class ReadmeSnippets {

    private final AuthenticationClient client = AuthenticationClients.builder().build();

    private void createClient() {
        AuthenticationClient client = AuthenticationClients.builder()
            .setOrgUrl("https://{yourOktaDomain}")
            .build();
    }

    private void headersAndQuery() {
        List<Header> headers = new ArrayList<>();
        headers.add(new Header("aHeaderName", "aValue")); // set any header
        headers.add(Header.xForwardedFor("10.10.0.1")); // X-Forwarded-For
        headers.add(Header.xDeviceFingerprint("your-finger-print")); // X-Device-Fingerprint

        List<QueryParameter> queryParameters = new ArrayList<>();
        queryParameters.add(new QueryParameter("aQueryParam", "aValue")); // set query param

        RequestContext requestContext = new RequestContext(headers, queryParameters);
    }
}
