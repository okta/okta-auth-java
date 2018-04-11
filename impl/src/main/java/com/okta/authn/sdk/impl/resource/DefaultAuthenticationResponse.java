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

import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.authn.sdk.resource.AuthenticationStatus;
import com.okta.authn.sdk.resource.Factor;
import com.okta.authn.sdk.resource.Link;
import com.okta.authn.sdk.resource.User;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.DateProperty;
import com.okta.sdk.impl.resource.EnumProperty;
import com.okta.sdk.impl.resource.MapProperty;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DefaultAuthenticationResponse extends AbstractResource implements AuthenticationResponse {

    private static final StringProperty TYPE_PROPERTY = new StringProperty("type");

    private static final DateProperty EXPIRES_AT_PROPERTY = new DateProperty("expiresAt");

    private static final EnumProperty<AuthenticationStatus> STATUS_PROPERTY = new EnumProperty<>("status", AuthenticationStatus.class);

    private static final StringProperty FACTOR_RESULT_PROPERTY = new StringProperty("factorResult");

    private static final StringProperty FACTOR_RESULT_MESSAGE_PROPERTY = new StringProperty("factorResultMessage");

    private static final StringProperty STATE_TOKEN_PROPERTY = new StringProperty("stateToken");

    private static final StringProperty RELAY_STATE_PROPERTY = new StringProperty("relayState");

    private static final StringProperty RECOVERY_TOKEN_PROPERTY = new StringProperty("recoveryToken");

    private static final StringProperty SESSION_TOKEN_PROPERTY = new StringProperty("sessionToken");

    private static final StringProperty ID_TOKEN_PROPERTY = new StringProperty("idToken");

    private static final StringProperty FACTOR_TYPE_PROPERTY = new StringProperty("factorType");

    private static final StringProperty RECOVERY_TYPE_PROPERTY = new StringProperty("recoveryType");

    private static final MapProperty EMBEDDED_PROPERTY = new MapProperty("_embedded");

    private static final MapProperty LINKS_PROPERTY = new MapProperty("_links");

    // Nested under _embedded
    private static final StringProperty NESTED__USER_PROPERTY = new StringProperty("user");
    private static final StringProperty NESTED__FACTORS_PROPERTY = new StringProperty("factors");
    private static final StringProperty NESTED__FACTOR_PROPERTY = new StringProperty("factor");

    public DefaultAuthenticationResponse(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(
            TYPE_PROPERTY,
            EXPIRES_AT_PROPERTY,
            STATUS_PROPERTY,
            FACTOR_RESULT_PROPERTY,
            FACTOR_RESULT_MESSAGE_PROPERTY,
            STATE_TOKEN_PROPERTY,
            RELAY_STATE_PROPERTY,
            RECOVERY_TOKEN_PROPERTY,
            SESSION_TOKEN_PROPERTY,
            ID_TOKEN_PROPERTY,
            FACTOR_TYPE_PROPERTY,
            RECOVERY_TYPE_PROPERTY,
            EMBEDDED_PROPERTY,
            LINKS_PROPERTY
        );
    }

    @Override
    public String getStateToken() {
        return getString(STATE_TOKEN_PROPERTY);
    }

    @Override
    public String getType() {
        return getString(TYPE_PROPERTY);
    }

    @Override
    public Date getExpiresAt() {
        return getDateProperty(EXPIRES_AT_PROPERTY);
    }

    @Override
    public AuthenticationStatus getStatus() {
        return getEnumProperty(STATUS_PROPERTY);
    }

    @Override
    public String getStatusString() {
        return getString(STATUS_PROPERTY.getName());
    }

    @Override
    public String getFactorResult() {
        return getString(FACTOR_RESULT_PROPERTY);
    }

    @Override
    public String getFactorResultMessage() {
        return getString(FACTOR_RESULT_MESSAGE_PROPERTY);
    }

    @Override
    public String getRelayState() {
        return getString(RELAY_STATE_PROPERTY);
    }

    @Override
    public String getRecoveryToken() {
        return getString(RECOVERY_TOKEN_PROPERTY);
    }

    @Override
    public String getSessionToken() {
        return getString(SESSION_TOKEN_PROPERTY);
    }

    @Override
    public String getIdToken() {
        return getString(ID_TOKEN_PROPERTY);
    }

    @Override
    public String getFactorType() {
        return getString(FACTOR_TYPE_PROPERTY);
    }

    @Override
    public String getRecoveryType() {
        return getString(RECOVERY_TYPE_PROPERTY);
    }

    @Override
    public Map<String, Object> getEmbedded() {
        return getNonEmptyMap(EMBEDDED_PROPERTY);
    }

    @Override
    public Map<String, Link> getLinks() {
        return DefaultLink.getLinks(getMap(LINKS_PROPERTY), this.getDataStore());
    }

    @Override
    public User getUser() {
        Map<String, Object> userDetails = (Map) getEmbedded().get(NESTED__USER_PROPERTY.getName());
        if (userDetails != null) {
            return getDataStore().instantiate(User.class, userDetails);
        }
        return null;
    }

    @Override
    public List<Factor> getFactors() {
        List<Factor> result = new ArrayList<>();
        List<Map<String, Object>> rawFactors = (List<Map<String, Object>>) getEmbedded().get(NESTED__FACTORS_PROPERTY.getName());
        if (rawFactors != null) {
            rawFactors.forEach(rawFactor -> result.add(new DefaultFactor(getDataStore(), rawFactor)));
        } else {

            // FIXME: i don't like this getFactors and getFactor is already to confusing, maybe the challenge response needs a different type?

            // some times we only have a single factor
            Map<String, Object> rawFactor = (Map<String, Object>) getEmbedded().get(NESTED__FACTOR_PROPERTY.getName());
            if (rawFactor != null) {
                result.add(new DefaultFactor(getDataStore(), rawFactor));
            }
        }
        return result;
    }
}