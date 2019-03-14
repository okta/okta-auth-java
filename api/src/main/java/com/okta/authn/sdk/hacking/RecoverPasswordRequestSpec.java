package com.okta.authn.sdk.hacking;

import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.sdk.resource.user.factor.FactorType;

public class RecoverPasswordRequestSpec  extends BaseRequestSpec<RecoverPasswordRequestSpec> {

    RecoverPasswordRequestSpec relayState(String relayState) {
        return self();
    }

    public RecoverPasswordRequestSpec  setUsername(String username) {
        return self();
    }

    public RecoverPasswordRequestSpec  setFactorType(FactorType factorType) {
        return self();
    }

    @Override
    public AuthenticationResponse execute() {
        return null;
    }
}
