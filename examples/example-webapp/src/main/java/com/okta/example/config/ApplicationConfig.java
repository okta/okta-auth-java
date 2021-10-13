/*
 * Copyright (c) 2021-Present, Okta, Inc.
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
package com.okta.example.config;

import com.okta.authn.sdk.AuthenticationStateHandler;
import com.okta.authn.sdk.AuthenticationStateHandlerAdapter;
import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.client.AuthenticationClients;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    /**
     * The Authentication Client bean definition.
     *
     * @return the authentication client
     */
    @Bean
    public AuthenticationClient authenticationClient() {
        return AuthenticationClients.builder().build();
    }

    /**
     * The Authentication state handler bean definition.
     *
     * @return the authentication state handler
     */
    @Bean
    public AuthenticationStateHandler authenticationStateHandler() {
        return new AuthenticationStateHandlerAdapter() {
            @Override
            public void handleUnknown(AuthenticationResponse authenticationResponse) {}
        };
    }
}