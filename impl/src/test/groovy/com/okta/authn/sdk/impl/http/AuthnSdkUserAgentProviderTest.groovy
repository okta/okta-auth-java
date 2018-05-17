/*
 * Copyright 2014 Stormpath, Inc.
 * Modifications Copyright 2018 Okta, Inc.
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
package com.okta.authn.sdk.impl.http

import com.okta.sdk.impl.http.support.UserAgent
import com.okta.sdk.impl.http.support.Version
import com.okta.sdk.lang.Strings
import org.testng.annotations.Test

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

/**
 * @since 0.5.0
 */
class AuthnSdkUserAgentProviderTest {

    private static final String VERSION_SEPARATOR = "/"
    private static final String ENTRY_SEPARATOR = " "
    private static final String SDK_KEY = "okta-sdk-java"
    private static final String AUTHN_KEY = "okta-auth-java"

    private static final String AUTHN_VERSION = Version.getClientVersion(AuthnSdkUserAgentProvider.VERSION_FILE)
    private static final String SDK_VERSION = Version.getClientVersion()

    @Test
    void testGetUserAgentFromProvider() {
        String userAgent = new AuthnSdkUserAgentProvider().getUserAgent()
        assertThat userAgent, is(AUTHN_KEY + VERSION_SEPARATOR + AUTHN_VERSION)
    }

    @Test
    void testGetUserAgentString() {

        String userAgent = UserAgent.getUserAgentString()
        assertThat userAgent, allOf(
                not(emptyString()),
                containsString(SDK_KEY + VERSION_SEPARATOR + SDK_VERSION + ENTRY_SEPARATOR),
                containsString(AUTHN_KEY + VERSION_SEPARATOR + AUTHN_VERSION + ENTRY_SEPARATOR),
                containsString("java" + VERSION_SEPARATOR + System.getProperty("java.version") + ENTRY_SEPARATOR)
        )
        assertThat "Expected '${SDK_KEY}' to appear in userAgent once once.", Strings.countOccurrencesOf(userAgent, SDK_KEY), equalTo(1)
    }
}
