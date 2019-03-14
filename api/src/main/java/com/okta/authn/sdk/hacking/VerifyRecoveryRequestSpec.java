package com.okta.authn.sdk.hacking;

import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.sdk.resource.user.factor.FactorType;

public class VerifyRecoveryRequestSpec extends BaseRequestSpec<VerifyRecoveryRequestSpec> {

    public VerifyRecoveryRequestSpec factorType(FactorType factorType) {
        return self();
    }

    public VerifyRecoveryRequestSpec setPassCode(String passCode) {
        return self();
    }

    @Override
    public AuthenticationResponse execute() {
        return null;
    }
}
