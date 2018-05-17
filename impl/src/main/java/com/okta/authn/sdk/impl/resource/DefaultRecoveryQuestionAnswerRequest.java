/*
 * Copyright 2018 Okta, Inc.
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
