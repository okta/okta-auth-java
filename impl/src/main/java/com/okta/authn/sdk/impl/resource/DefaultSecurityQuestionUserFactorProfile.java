package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.SecurityQuestionUserFactorProfile;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultSecurityQuestionUserFactorProfile extends AbstractResource implements SecurityQuestionUserFactorProfile {

    private final static StringProperty answerProperty = new StringProperty("answer");
    private final static StringProperty questionProperty = new StringProperty("question");
    private final static StringProperty questionTextProperty = new StringProperty("questionText");

    private final static Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(answerProperty, questionProperty, questionTextProperty);

    public DefaultSecurityQuestionUserFactorProfile(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultSecurityQuestionUserFactorProfile(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return PROPERTY_DESCRIPTORS;
    }


    public String getAnswer() {
        return  getString(answerProperty);
    }

    public SecurityQuestionUserFactorProfile setAnswer(String answer) {
        setProperty(answerProperty, answer);
        return this;
    }

    public String getQuestion() {
        return  getString(questionProperty);
    }

    public SecurityQuestionUserFactorProfile setQuestion(String question) {
        setProperty(questionProperty, question);
        return this;
    }

    public String getQuestionText() {
        return  getString(questionTextProperty);
    }

    public SecurityQuestionUserFactorProfile setQuestionText(String questionText) {
        setProperty(questionTextProperty, questionText);
        return this;
    }

}
