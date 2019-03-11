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

import static com.okta.authn.sdk.http.RequestParameterMatcher.isQuery
import static org.hamcrest.MatcherAssert.assertThat

import static com.okta.authn.sdk.http.RequestParameterMatcher.isHeader

class HeaderTest {

    @Test
    void valuesTest() {
        assertThat new Header("aKey", "aValue"), isHeader("aKey", ["aValue"])
        assertThat new Header("aKey", ["aValue"]), isHeader("aKey", ["aValue"])
        assertThat new Header("aKey", ["aValue1", "aValue2"]), isHeader("aKey", ["aValue1", "aValue2"])
        assertThat new QueryParameter(null, "aValue"), isQuery(null, "aValue")
        assertThat new QueryParameter(null, null), isQuery(null, null)
        assertThat new QueryParameter("aKey", null), isQuery("aKey", null)
    }
}