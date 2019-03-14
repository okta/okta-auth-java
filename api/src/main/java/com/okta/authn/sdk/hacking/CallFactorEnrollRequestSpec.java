package com.okta.authn.sdk.hacking;

import com.okta.authn.sdk.resource.AuthenticationResponse;

public class CallFactorEnrollRequestSpec extends BaseRequestSpec<CallFactorEnrollRequestSpec> {

    public CallFactorEnrollRequestSpec phoneExtension(String phoneExtension) {
        return self();
    }

    public CallFactorEnrollRequestSpec phoneNumber(String phoneNumber) {
        return self();
    }

    @Override
    public AuthenticationResponse execute() {
        return null;
    }
}
