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
package com.okta.authn.sdk.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.okta.authn.sdk.http.Header.header;

public final class RequestContext {

    private final List<Header> headers = new ArrayList<>();
    private final List<QueryParameter> queryParams = new ArrayList<>();

    public RequestContext() {}

    public RequestContext(List<Header> headers, List<QueryParameter> queryParams) {
        this.headers.addAll(headers);
        this.queryParams.addAll(queryParams);
    }

    public RequestContext(Map<String, List<String>> headers, Map<String, String> queryParams) {

        headers.entrySet().stream()
            .map(entry -> new Header(entry.getKey(), entry.getValue()))
            .forEach(this.headers::add);

        queryParams.entrySet().stream()
            .map(entry -> new QueryParameter(entry.getKey(), entry.getValue()))
            .forEach(this.queryParams::add);
    }

    public List<Header> getHeaders() {
        return Collections.unmodifiableList(headers);
    }

    public List<QueryParameter> getQueryParams() {
        return Collections.unmodifiableList(queryParams);
    }

    public RequestContext addHeader(Header header) {
        headers.add(header);
        return this;
    }

    public RequestContext addQuery(QueryParameter query) {
        queryParams.add(query);
        return this;
    }
}