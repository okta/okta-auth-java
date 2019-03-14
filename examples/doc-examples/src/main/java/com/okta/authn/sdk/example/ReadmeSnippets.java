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
package com.okta.authn.sdk.example;

import com.okta.authn.sdk.AuthenticationException;
import com.okta.authn.sdk.AuthenticationStateHandler;
import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.client.AuthenticationClients;
import com.okta.sdk.resource.user.factor.CallFactorProfile;
import com.okta.sdk.resource.user.factor.FactorProvider;
import com.okta.sdk.resource.user.factor.FactorType;
import com.okta.sdk.resource.user.factor.PushFactorProfile;
import com.okta.sdk.resource.user.factor.TotpFactor;
import com.okta.sdk.resource.user.factor.TotpFactorProfile;

@SuppressWarnings({"unused"})
public class ReadmeSnippets {

    private final AuthenticationClient client = AuthenticationClients.builder().build();

    private void createClient() throws AuthenticationException {
        AuthenticationClient client = AuthenticationClients.builder()
            .setOrgUrl("https://{yourOktaDomain}")
            .build();

        AuthenticationStateHandler stateHandler = null;

        client.authenticate()
                .username("joe.coder@example.com")
                .password("aPassword".toCharArray())
                .contextItem("some-key", "some-value")
                .contextItem("another-key", "another-value")
                .warnBeforePasswordExpired(true)
                .header("some-httpHeader", "a header")
                .query("a-query-param", "value")
                .stateHandler(stateHandler)
                .execute();

        client.authenticate()
                .username("joe.coder@example.com")
                .password("aPassword".toCharArray())
                .execute();

        client.enrollFactor()
                .factorType(FactorType.TOKEN_SOFTWARE_TOTP)
                .provider(FactorProvider.OKTA)
                .factorProfile(client.instantiate(TotpFactorProfile.class)
                                            .setCredentialId("credentialId"))
                .execute();

        client.enrollFactor()
                .factorType(FactorType.PUSH)
                .provider(FactorProvider.OKTA)
                .factorProfile(client.instantiate(PushFactorProfile.class)
                                            .setCredentialId("credentialId")
                                            .setName("ummm")
                                            .setVersion("1.0"))
                .execute();

        client.enrollFactor()
                .factorType(FactorType.CALL)
                .factorProfile(client.instantiate(CallFactorProfile.class)
                                            .setPhoneNumber("603 555 1234"))
                .execute();

        client.enrollFactor().callFactor()
                .phoneNumber("603 555 1234")
                .execute();

        client.enrollFactor().totpFactor()
                .sharedSecret("something")
                .execute();
    }
}
