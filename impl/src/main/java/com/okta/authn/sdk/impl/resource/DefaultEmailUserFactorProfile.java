package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.EmailUserFactorProfile;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultEmailUserFactorProfile extends AbstractResource implements EmailUserFactorProfile {

    private final static StringProperty emailProperty = new StringProperty("email");

    private final static Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(emailProperty);

    public DefaultEmailUserFactorProfile(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultEmailUserFactorProfile(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return PROPERTY_DESCRIPTORS;
    }


    public String getEmail() {
        return  getString(emailProperty);
    }

    public EmailUserFactorProfile setEmail(String email) {
        setProperty(emailProperty, email);
        return this;
    }

}
