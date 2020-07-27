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

import com.okta.authn.sdk.resource.CallFactorProfile;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultCallFactorProfile extends AbstractResource implements CallFactorProfile {

    private final static StringProperty phoneExtensionProperty = new StringProperty("phoneExtension");
    private final static StringProperty phoneNumberProperty = new StringProperty("phoneNumber");

    private final static Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(phoneExtensionProperty, phoneNumberProperty);

    public DefaultCallFactorProfile(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultCallFactorProfile(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return PROPERTY_DESCRIPTORS;
    }


    public String getPhoneExtension() {
        return  getString(phoneExtensionProperty);
    }

    public CallFactorProfile setPhoneExtension(String phoneExtension) {
        setProperty(phoneExtensionProperty, phoneExtension);
        return this;
    }

    public String getPhoneNumber() {
        return  getString(phoneNumberProperty);
    }

    public CallFactorProfile setPhoneNumber(String phoneNumber) {
        setProperty(phoneNumberProperty, phoneNumber);
        return this;
    }

}
