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
package com.okta.authn.sdk.impl.client

import com.okta.sdk.impl.http.*
import com.okta.sdk.impl.http.support.DefaultResponse
import com.okta.sdk.impl.io.ClasspathResource
import groovy.text.StreamingTemplateEngine
import org.hamcrest.Matcher

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

class StubRequestExecutor implements RequestExecutor {

    List<Matcher<Request>> requestMatchers = []
    Map<String, Object> interpolationData = new HashMap<>()

    @Override
    Response executeRequest(Request request) throws RestException {

        assertThat(request.headers, allOf(
                hasEntry(equalTo(HttpHeaders.ACCEPT), everyItem(equalTo(MediaType.APPLICATION_JSON_VALUE))),
                hasEntry(equalTo("User-Agent"), everyItem(notNullValue())),
                not(hasKey(equalTo("Authorization")))
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