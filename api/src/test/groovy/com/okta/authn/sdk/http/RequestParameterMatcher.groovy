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

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class RequestParameterMatcher<K, V> extends TypeSafeMatcher<RequestParameter<K, V>> {

    private final K key
    private final V value

    RequestParameterMatcher(Class<?> expectedType, K key, V value) {
        super(expectedType)
        this.key = key
        this.value = value
    }

    @Override
    protected boolean matchesSafely(RequestParameter<K, V> actual) {
        return actual != null &&
            key.equals(actual.getKey()) &&
            value.equals(actual.getValue())
    }

    @Override
    void describeMismatchSafely(RequestParameter<K, V> actual, Description mismatchDescription) {
        describe(mismatchDescription.appendText("was: "), actual)
    }

    @Override
    void describeTo(Description description) {
        describe(description, key, value)
    }

    Description describe(Description description, RequestParameter<K, V> requestParameter) {
        return description == null ?
            description.appendValue(null) :
            describe(description, requestParameter.key, requestParameter.value)
    }

    Description describe(Description description, K key, V value) {
        description.appendText("a key: ")
            .appendText(key)
            .appendText(" with value: ")
            .appendValue(value)
    }


    static RequestParameterMatcher<String, List<String>> isHeader(String key, List<String> value) {
        return new RequestParameterMatcher(Header, key, value)
    }

    static RequestParameterMatcher<String, String> isQuery(String key, String value) {
        return new RequestParameterMatcher(QueryParameter, key, value)
    }
}