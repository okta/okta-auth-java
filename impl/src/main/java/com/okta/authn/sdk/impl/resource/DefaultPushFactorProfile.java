/*
 * Copyright 2017-Present Okta, Inc.
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

import com.okta.authn.sdk.resource.PushFactorProfile;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultPushFactorProfile extends AbstractResource implements PushFactorProfile {

    private final static StringProperty credentialIdProperty = new StringProperty("credentialId");
    private final static StringProperty deviceTokenProperty = new StringProperty("deviceToken");
    private final static StringProperty deviceTypeProperty = new StringProperty("deviceType");
    private final static StringProperty nameProperty = new StringProperty("name");
    private final static StringProperty platformProperty = new StringProperty("platform");
    private final static StringProperty versionProperty = new StringProperty("version");

    private final static Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(credentialIdProperty, deviceTokenProperty, deviceTypeProperty, nameProperty, platformProperty, versionProperty);

    public DefaultPushFactorProfile(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultPushFactorProfile(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return PROPERTY_DESCRIPTORS;
    }


    public String getCredentialId() {
        return  getString(credentialIdProperty);
    }

    public PushFactorProfile setCredentialId(String credentialId) {
        setProperty(credentialIdProperty, credentialId);
        return this;
    }

    public String getDeviceToken() {
        return  getString(deviceTokenProperty);
    }

    public PushFactorProfile setDeviceToken(String deviceToken) {
        setProperty(deviceTokenProperty, deviceToken);
        return this;
    }

    public String getDeviceType() {
        return  getString(deviceTypeProperty);
    }

    public PushFactorProfile setDeviceType(String deviceType) {
        setProperty(deviceTypeProperty, deviceType);
        return this;
    }

    public String getName() {
        return  getString(nameProperty);
    }

    public PushFactorProfile setName(String name) {
        setProperty(nameProperty, name);
        return this;
    }

    public String getPlatform() {
        return  getString(platformProperty);
    }

    public PushFactorProfile setPlatform(String platform) {
        setProperty(platformProperty, platform);
        return this;
    }

    public String getVersion() {
        return  getString(versionProperty);
    }

    public PushFactorProfile setVersion(String version) {
        setProperty(versionProperty, version);
        return this;
    }

}
