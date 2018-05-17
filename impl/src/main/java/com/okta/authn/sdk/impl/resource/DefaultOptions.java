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

import com.okta.authn.sdk.resource.Options;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.BooleanProperty;
import com.okta.sdk.impl.resource.Property;

import java.util.Map;

public class DefaultOptions extends AbstractResource implements Options {

    private static final BooleanProperty MULTI_OPTIONAL_FACTOR_ENROLL_PROPERTY = new BooleanProperty("multiOptionalFactorEnroll");
    private static final BooleanProperty WARN_BEFORE_PASSWORD_EXPIRED_PROPERTY = new BooleanProperty("warnBeforePasswordExpired");

    public DefaultOptions(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultOptions(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(MULTI_OPTIONAL_FACTOR_ENROLL_PROPERTY, WARN_BEFORE_PASSWORD_EXPIRED_PROPERTY);
    }

    @Override
    public Boolean isMultiOptionalFactorEnroll() {
        return getBoolean(MULTI_OPTIONAL_FACTOR_ENROLL_PROPERTY);
    }

    @Override
    public Options setMultiOptionalFactorEnroll(Boolean multiOptionalFactorEnroll) {
        setProperty(MULTI_OPTIONAL_FACTOR_ENROLL_PROPERTY, multiOptionalFactorEnroll);
        return this;
    }

    @Override
    public Boolean isWarnBeforePasswordExpired() {
        return getBoolean(WARN_BEFORE_PASSWORD_EXPIRED_PROPERTY);
    }

    @Override
    public Options setWarnBeforePasswordExpired(Boolean warnBeforePasswordExpired) {
        setProperty(WARN_BEFORE_PASSWORD_EXPIRED_PROPERTY, warnBeforePasswordExpired);
        return this;
    }
}