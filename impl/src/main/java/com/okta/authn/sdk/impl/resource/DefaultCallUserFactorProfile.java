package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.CallUserFactorProfile;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultCallUserFactorProfile extends AbstractResource implements CallUserFactorProfile {

    private final static StringProperty phoneExtensionProperty = new StringProperty("phoneExtension");
    private final static StringProperty phoneNumberProperty = new StringProperty("phoneNumber");

    private final static Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(phoneExtensionProperty, phoneNumberProperty);

    public DefaultCallUserFactorProfile(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultCallUserFactorProfile(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return PROPERTY_DESCRIPTORS;
    }


    public String getPhoneExtension() {
        return  getString(phoneExtensionProperty);
    }

    public CallUserFactorProfile setPhoneExtension(String phoneExtension) {
        setProperty(phoneExtensionProperty, phoneExtension);
        return this;
    }

    public String getPhoneNumber() {
        return  getString(phoneNumberProperty);
    }

    public CallUserFactorProfile setPhoneNumber(String phoneNumber) {
        setProperty(phoneNumberProperty, phoneNumber);
        return this;
    }

}
