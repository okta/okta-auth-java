package com.okta.authn.sdk.resource;

import com.okta.sdk.resource.Resource;

public interface RecoveryQuestionAnswerRequest extends Resource {

    String getStateToken();
    RecoveryQuestionAnswerRequest setStateToken(String stateToken);

    String getAnswer();
    RecoveryQuestionAnswerRequest setAnswer(String answer);
}
