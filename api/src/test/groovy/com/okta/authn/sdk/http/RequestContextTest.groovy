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

import org.hamcrest.MatcherAssert
import org.testng.annotations.Test

import static com.okta.authn.sdk.http.Header.header
import static com.okta.authn.sdk.http.QueryParameter.query
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*
import static com.okta.authn.sdk.http.RequestParameterMatcher.isHeader
import static com.okta.authn.sdk.http.RequestParameterMatcher.isQuery

class RequestContextTest {

    @Test
    void emptyTest() {
        RequestContext context = new RequestContext()
        assertThat context.getHeaders(), hasSize(0)
        assertThat context.getQueryParams(), hasSize(0)
    }

    @Test
    void constructorValuesTest() {
        RequestContext context = new RequestContext([header("header1", "hvalue1"), header("header2", "hvalue2")], [query("query1", "qvalue1")])
        assertThat context.getHeaders(), contains(isHeader("header1", ["hvalue1"]),
                                                  isHeader("header2", ["hvalue2"]))
        assertThat context.getQueryParams(), contains(query("query1", "qvalue1"))
    }

    @Test
    void constructorMapsTest() {
        RequestContext context = new RequestContext([header1: ["hvalue1"], header2: ["hvalue2"]], [query1: "qvalue1"])
        assertThat context.getHeaders(), contains(isHeader("header1", ["hvalue1"]),
                                                  isHeader("header2", ["hvalue2"]))
        assertThat context.getQueryParams(), contains(query("query1", "qvalue1"))
    }

    @Test
    void addHeadersTest() {
        RequestContext context = new RequestContext().addHeader(header("header1", "hvalue1"))
        assertThat context.getHeaders(), contains(isHeader("header1", ["hvalue1"]))
    }

    @Test
    void addQueryParamsTest() {
        RequestContext context = new RequestContext().addQuery(query("query1", "qvalue1"))
        assertThat context.getQueryParams(), contains(isQuery("query1", "qvalue1"))
    }
}
