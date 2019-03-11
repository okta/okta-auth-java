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
