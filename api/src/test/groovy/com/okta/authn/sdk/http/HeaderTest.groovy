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
package com.okta.authn.sdk.http

import org.testng.annotations.Test

import static com.okta.authn.sdk.http.Header.header
import static com.okta.authn.sdk.http.Header.xForwardedFor
import static com.okta.authn.sdk.http.Header.xDeviceFingerprint
import static com.okta.authn.sdk.http.Header.acceptLanguage
import static com.okta.authn.sdk.http.Header.userAgent
import static org.hamcrest.MatcherAssert.assertThat

import static com.okta.authn.sdk.http.RequestParameterMatcher.isHeader

class HeaderTest {

    @Test
    void valuesTest() {
        assertThat new Header("aKey", "aValue"), isHeader("aKey", ["aValue"])
        assertThat new Header("aKey", ["aValue"]), isHeader("aKey", ["aValue"])
        assertThat new Header("aKey", ["aValue1", "aValue2"]), isHeader("aKey", ["aValue1", "aValue2"])
        assertThat new Header(null, "aValue"), isHeader(null, ["aValue"])
        assertThat new Header(null, null), isHeader(null, null)
        assertThat new Header("aKey", null), isHeader("aKey", null)
        assertThat header("aKey", "aValue"), isHeader("aKey", ["aValue"])
        assertThat header("aKey", ["aValue"]), isHeader("aKey", ["aValue"])
        assertThat xForwardedFor("10.10.0.1"), isHeader("X-Forwarded-For", ["10.10.0.1"])
        assertThat xDeviceFingerprint("some-hash"), isHeader("X-Device-Fingerprint", ["some-hash"])
        assertThat acceptLanguage("en-us"), isHeader("Accept-Language", ["en-us"])
        assertThat userAgent("something/1.0"), isHeader("User-Agent", ["something/1.0"])
    }
}