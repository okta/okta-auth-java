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
package com.okta.authn.sdk.hacking;

import com.okta.authn.sdk.AuthenticationStateHandler;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.authn.sdk.resource.ChangePasswordRequest;

import java.util.List;

public abstract class BaseRequestSpec<SELF extends BaseRequestSpec<SELF>> {

    public SELF query(String key, String value) {
        return self();
    }

    public SELF header(String key, String value) {
        return self();
    }

    public SELF header(String key, List<String> value) {
        return self();
    }

    public SELF setStateToken(String stateToken) {
        return self();
    }

    public SELF stateHandler(AuthenticationStateHandler stateHandler) {
        return self();
    }

    protected SELF self() {
        return (SELF) this;
    }



    public abstract AuthenticationResponse execute();
}
