package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.RecoveryQuestionAnswerRequest;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultRecoveryQuestionAnswerRequest extends AbstractResource implements RecoveryQuestionAnswerRequest {

    private static final StringProperty STATE_TOKEN_PROPERTY = new StringProperty("stateToken");

    private static final StringProperty ANSWER_PROPERTY = new StringProperty("answer");

    public DefaultRecoveryQuestionAnswerRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultRecoveryQuestionAnswerRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(STATE_TOKEN_PROPERTY, ANSWER_PROPERTY);
    }

    @Override
    public String getStateToken() {
        return getString(STATE_TOKEN_PROPERTY);
    }

    @Override
    public RecoveryQuestionAnswerRequest setStateToken(String stateToken) {
        setProperty(STATE_TOKEN_PROPERTY, stateToken);
        return this;
    }

    @Override
    public String getAnswer() {
        return getString(ANSWER_PROPERTY);
    }

    @Override
    public RecoveryQuestionAnswerRequest setAnswer(String answer) {
        setProperty(ANSWER_PROPERTY, answer);
        return this;
    }
}
