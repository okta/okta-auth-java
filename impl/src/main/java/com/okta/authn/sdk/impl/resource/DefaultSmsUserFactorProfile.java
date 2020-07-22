package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.SmsUserFactorProfile;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultSmsUserFactorProfile extends AbstractResource implements SmsUserFactorProfile {

    private final static StringProperty phoneNumberProperty = new StringProperty("phoneNumber");

    private final static Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(phoneNumberProperty);

    public DefaultSmsUserFactorProfile(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultSmsUserFactorProfile(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return PROPERTY_DESCRIPTORS;
    }


    public String getPhoneNumber() {
        return  getString(phoneNumberProperty);
    }

    public SmsUserFactorProfile setPhoneNumber(String phoneNumber) {
        setProperty(phoneNumberProperty, phoneNumber);
        return this;
    }

}
