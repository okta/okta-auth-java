/*
 * Copyright 2020-Present Okta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
