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
package com.okta.authn.sdk.its.email

import com.fasterxml.jackson.annotation.JsonProperty

class Email {

    private String alias
    private String email
    private long timestamp
    private String token

    String getAlias() {
        return alias
    }

    void setAlias(String alias) {
        this.alias = alias
    }

    String getEmail() {
        return email
    }

    @JsonProperty("email_addr")
    void setEmail(String email) {
        this.email = email
    }

    long getTimestamp() {
        return timestamp
    }

    @JsonProperty("email_timestamp")
    void setTimestamp(long timestamp) {
        this.timestamp = timestamp
    }

    String getToken() {
        return token
    }

    @JsonProperty("sid_token")
    void setToken(String token) {
        this.token = token
    }
}