package com.okta.authn.sdk.hacking;

import com.okta.authn.sdk.resource.AuthenticationResponse;

public class VerifyRecoveryTokenRequestSpec extends BaseRequestSpec<VerifyRecoveryTokenRequestSpec> {

    public VerifyRecoveryTokenRequestSpec recoveryToken(String recoveryToken) {
        return self();
    }

    @Override
    public AuthenticationResponse execute() {
        return null;
    }
}
