package com.okta.authn.sdk.example;

import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.client.AuthenticationClients;

@SuppressWarnings({"unused"})
public class ReadmeSnippets {

    private final AuthenticationClient client = AuthenticationClients.builder().build();

    private void createClient() {
        AuthenticationClient client = AuthenticationClients.builder()
            .setOrgUrl("https://dev-123456.oktapreview.com/")
            .build();
    }
}
