package com.okta.authn.sdk.hacking;

import com.okta.authn.sdk.resource.AuthenticationResponse;

public class RecoveryQuestionAnswerRequestSpec extends BaseRequestSpec<RecoveryQuestionAnswerRequestSpec> {

    public RecoveryQuestionAnswerRequestSpec setAnswer(String answer) {
        return self();
    }

    @Override
    public AuthenticationResponse execute() {
        return null;
    }
}
