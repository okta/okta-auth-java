package com.okta.authn.sdk.hacking;

import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.sdk.resource.user.factor.FactorType;

public class UnlockAccountRequestSpec extends BaseRequestSpec<UnlockAccountRequestSpec> {

    public UnlockAccountRequestSpec  setRelayState(String relayState) {
        return self();
    }

    public UnlockAccountRequestSpec  setUsername(String username) {
        return self();
    }

    public UnlockAccountRequestSpec  setFactorType(FactorType factorType) {
        return self();
    }

    @Override
    public AuthenticationResponse execute() {
        return null;
    }
}
