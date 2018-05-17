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

import com.okta.authn.sdk.resource.User;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.DateProperty;
import com.okta.sdk.impl.resource.MapProperty;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;
import com.okta.sdk.lang.Locales;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DefaultUser extends AbstractResource implements User {

    private static final StringProperty ID_PROPERTY = new StringProperty("id");

    private static final DateProperty PASSWORD_CHANGED_PROPERTY = new DateProperty("passwordChanged");

    private static final MapProperty PROFILE_PROPERTY = new MapProperty("profile");

    private static final MapProperty RECOVERY_QUESTION_PROPERTY = new MapProperty("recovery_question");

    // nested properties exposed directly on this object for ease of use
    private static final StringProperty NESTED__LOGIN_PROPERTY = new StringProperty("login");
    private static final StringProperty NESTED__FIRST_NAME_PROPERTY = new StringProperty("firstName");
    private static final StringProperty NESTED__LAST_NAME_PROPERTY = new StringProperty("lastName");
    private static final StringProperty NESTED__LOCALE_PROPERTY = new StringProperty("locale");
    private static final StringProperty NESTED__TIME_ZONE_PROPERTY = new StringProperty("timeZone");

    public DefaultUser(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(
            ID_PROPERTY,
            PASSWORD_CHANGED_PROPERTY,
            PROFILE_PROPERTY,
            RECOVERY_QUESTION_PROPERTY
        );
    }

    @Override
    public String getId() {
        return getString(ID_PROPERTY);
    }

    @Override
    public Date getPasswordChanged() {
        return getDateProperty(PASSWORD_CHANGED_PROPERTY);
    }

    @Override
    public Map<String, String> getProfile() {
        return getNonEmptyMap(PROFILE_PROPERTY);
    }

    @Override
    public Map<String, String> getRecoveryQuestion() {
        return getNonEmptyMap(RECOVERY_QUESTION_PROPERTY);
    }

    @Override
    public String getLogin() {
        return getProfile().get(NESTED__LOGIN_PROPERTY.getName());
    }

    @Override
    public String getFirstName() {
        return getProfile().get(NESTED__FIRST_NAME_PROPERTY.getName());
    }

    @Override
    public String getLastName() {
        return getProfile().get(NESTED__LAST_NAME_PROPERTY.getName());
    }

    @Override
    public Locale getLocale() {
        String rawLocale = getProfile().get(NESTED__LOCALE_PROPERTY.getName());
        return Locales.toLocale(rawLocale);
    }

    @Override
    public TimeZone getTimeZone() {
        String rawTimeZone = getProfile().get(NESTED__TIME_ZONE_PROPERTY.getName());
        return TimeZone.getTimeZone(rawTimeZone);
    }
}