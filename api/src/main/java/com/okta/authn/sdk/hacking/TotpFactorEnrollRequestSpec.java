package com.okta.authn.sdk.hacking;

import com.okta.authn.sdk.resource.AuthenticationResponse;

public class TotpFactorEnrollRequestSpec extends BaseRequestSpec<TotpFactorEnrollRequestSpec> {

    public TotpFactorEnrollRequestSpec sharedSecret(String sharedSecret) {
        return self();
    }

    @Override
    public AuthenticationResponse execute() {
        return null;
    }
}
