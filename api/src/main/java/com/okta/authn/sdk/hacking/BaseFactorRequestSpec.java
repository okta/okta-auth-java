package com.okta.authn.sdk.hacking;

abstract class BaseFactorRequestSpec<SELF extends BaseRequestSpec<SELF>> extends BaseRequestSpec<SELF> {

    public SELF factorId(String factorId) {
        return self();
    }
}