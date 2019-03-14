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

import java.util.Collections;
import java.util.List;

public final class Header extends BaseRequestParameter<String, List<String>> implements RequestParameter {

    public Header(String key, String value) {
        this(key, Collections.singletonList(value));
    }

    public Header(String key, List<String> value) {
        super(key, value);
    }

    public static Header header(String key, String value) {
        return new Header(key, value);
    }

    public static Header header(String key, List<String> value) {
        return new Header(key, value);
    }

    public static Header xForwardedFor(String ipAddress) {
        return new Header("X-Forwarded-For", ipAddress);
    }

    public static Header xDeviceFingerprint(String fingerprint) {
        return new Header("X-Device-Fingerprint", fingerprint);
    }

    public static Header acceptLanguage(String fingerprint) {
        return new Header("Accept-Language", fingerprint);
    }

    public static Header userAgent(String fingerprint) {
        return new Header("User-Agent", fingerprint);
    }
}