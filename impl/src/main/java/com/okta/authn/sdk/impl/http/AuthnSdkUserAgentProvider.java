package com.okta.authn.sdk.impl.http;

import com.okta.sdk.http.UserAgentProvider;
import com.okta.sdk.impl.http.support.Version;

public class AuthnSdkUserAgentProvider implements UserAgentProvider {

    private static final String OKTA_AUTHN_STRING = "okta-auth-java";
    private static final String VERSION_SEPARATOR = "/";
    static final String VERSION_FILE = "/com/okta/authn/sdk/version.properties";

    @Override
    public String getUserAgent() {
        return OKTA_AUTHN_STRING + VERSION_SEPARATOR + Version.getClientVersion(VERSION_FILE);
    }
}
