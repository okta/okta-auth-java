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
package com.okta.authn.sdk.example.views.authn;

import com.okta.sdk.error.Error;
import io.dropwizard.views.View;

import java.util.Optional;

public class LoginView extends View {

//    private final Login login;
    private final Optional<Error> error;

    public LoginView(Optional<Error> error) {
        super("login.mustache");
        this.error = error;
//        this.login = login;
    }

//    public Login getLogin() {
//        return login;
//    }

    public Optional<Error> getError() {
        return error;
    }
}