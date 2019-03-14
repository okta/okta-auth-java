package com.okta.authn.sdk.hacking;

import com.okta.authn.sdk.resource.AuthenticationResponse;

public class SkipRequestSpec extends BaseRequestSpec<SkipRequestSpec> {

    @Override
    public AuthenticationResponse execute() {
        return null;
    }
}
