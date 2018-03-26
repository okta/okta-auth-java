package com.okta.authn.sdk.impl.client

import com.okta.sdk.impl.http.MediaType
import com.okta.sdk.impl.http.Request
import com.okta.sdk.impl.http.RequestExecutor
import com.okta.sdk.impl.http.Response
import com.okta.sdk.impl.http.RestException
import com.okta.sdk.impl.http.support.DefaultResponse
import com.okta.sdk.impl.io.ClasspathResource
import groovy.text.StreamingTemplateEngine
import org.apache.http.HttpHeaders
import org.hamcrest.Matcher

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.allOf
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.everyItem
import static org.hamcrest.Matchers.hasEntry
import static org.hamcrest.Matchers.hasKey
import static org.hamcrest.Matchers.not
import static org.hamcrest.Matchers.notNullValue

class StubRequestExecutor implements RequestExecutor {

    List<Matcher<Request>> requestMatchers = []
    Map<String, Object> interpolationData = new HashMap<>()

    @Override
    Response executeRequest(Request request) throws RestException {

        assertThat(request.headers, allOf(
                hasEntry(equalTo(HttpHeaders.ACCEPT), everyItem(equalTo(MediaType.APPLICATION_JSON_VALUE))),
                hasEntry(equalTo(HttpHeaders.USER_AGENT), everyItem(notNullValue())),
                not(hasKey(equalTo(HttpHeaders.AUTHORIZATION)))
        ))

        requestMatchers.forEach {
            assertThat request, it
        }

        def jsonResource = new ClasspathResource("${request.resourceUrl.host.replaceAll("\\.", "/")}${request.resourceUrl.path}.json")

        assertThat "Resource does not exist: "+ jsonResource.location, jsonResource.inputStream, notNullValue()


        String body = new StreamingTemplateEngine().createTemplate(new InputStreamReader(jsonResource.inputStream)).make(interpolationData).toString()
        new DefaultResponse(200, MediaType.APPLICATION_JSON, new ByteArrayInputStream(body.bytes), body.length())
    }
}