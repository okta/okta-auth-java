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

import com.okta.authn.sdk.resource.DuoFactorActivation;
import com.okta.authn.sdk.resource.FactorActivation;
import com.okta.authn.sdk.resource.Link;
import com.okta.authn.sdk.resource.PushFactorActivation;
import com.okta.authn.sdk.resource.TotpFactorActivation;
import com.okta.authn.sdk.resource.U2fFactorActivation;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.DateProperty;
import com.okta.sdk.impl.resource.IntegerProperty;
import com.okta.sdk.impl.resource.MapProperty;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Date;
import java.util.Map;

public class DefaultFactorActivation extends AbstractResource implements FactorActivation,
                                                                         TotpFactorActivation,
                                                                         U2fFactorActivation,
                                                                         PushFactorActivation,
                                                                         DuoFactorActivation {

    // totp
    private static final IntegerProperty TIME_STEP_PROPERTY = new IntegerProperty("timeStep");

    private static final StringProperty SHARED_SECRET_PROPERTY = new StringProperty("sharedSecret");

    private static final StringProperty ENCODING_PROPERTY = new StringProperty("encoding");

    private static final IntegerProperty KEY_LENGTH_PROPERTY = new IntegerProperty("keyLength");

    // push
    private static final DateProperty EXPIRES_AT = new DateProperty("expiresAt");

    // push, duo
    private static final StringProperty FACTOR_RESULT_PROPERTY = new StringProperty("factorResult");

    // duo
    private static final StringProperty HOST_PROPERTY = new StringProperty("host");

    private static final StringProperty SIGNATURE_PROPERTY = new StringProperty("signature");

    // u2f
    private static final StringProperty VERSION_PROPERTY = new StringProperty("version");

    private static final StringProperty APP_ID_PROPERTY = new StringProperty("appId");

    private static final StringProperty NONCE_PROPERTY = new StringProperty("nonce");

    private static final IntegerProperty TIMEOUT_PROPERTY = new IntegerProperty("timeoutSeconds");

    // any
    private static final MapProperty LINKS_PROPERTY = new MapProperty("_links");

    public DefaultFactorActivation(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(TIME_STEP_PROPERTY,
                                           SHARED_SECRET_PROPERTY,
                                           ENCODING_PROPERTY,
                                           KEY_LENGTH_PROPERTY,
                                           EXPIRES_AT,
                                           FACTOR_RESULT_PROPERTY,
                                           HOST_PROPERTY,
                                           SIGNATURE_PROPERTY ,
                                           VERSION_PROPERTY,
                                           APP_ID_PROPERTY,
                                           NONCE_PROPERTY,
                                           TIMEOUT_PROPERTY,
                                           LINKS_PROPERTY);
    }

    @Override
    public Integer getTimeStep() {
        return getIntProperty(TIME_STEP_PROPERTY);
    }

    @Override
    public String getSharedSecret() {
        return getString(SHARED_SECRET_PROPERTY);
    }

    @Override
    public String getEncoding() {
        return getString(ENCODING_PROPERTY);
    }

    @Override
    public Integer getKeyLength() {
        return getIntProperty(KEY_LENGTH_PROPERTY);
    }

    @Override
    public String getHost() {
        return getString(HOST_PROPERTY);
    }

    @Override
    public String getSignature() {
        return getString(SIGNATURE_PROPERTY);
    }

    @Override
    public String getFactorResult() {
        return getString(FACTOR_RESULT_PROPERTY);
    }

    @Override
    public Date getExpiresAt() {
        return getDateProperty(EXPIRES_AT);
    }

    @Override
    public String getVersion() {
        return getString(VERSION_PROPERTY);
    }

    @Override
    public String getAppId() {
        return getString(APP_ID_PROPERTY);
    }

    @Override
    public String getNonce() {
        return getString(NONCE_PROPERTY);
    }

    @Override
    public Integer getTimeoutSeconds() {
        return getIntProperty(TIMEOUT_PROPERTY);
    }

    @Override
    public Map<String, Link> getLinks() {
        return DefaultLink.getLinks(getMap(LINKS_PROPERTY), this.getDataStore());
    }
}
