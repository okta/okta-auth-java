package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.TotpUserFactorProfile;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultTotpUserFactorProfile extends AbstractResource implements TotpUserFactorProfile {

    private final static StringProperty credentialIdProperty = new StringProperty("credentialId");

    private final static Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(credentialIdProperty);

    public DefaultTotpUserFactorProfile(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultTotpUserFactorProfile(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return PROPERTY_DESCRIPTORS;
    }


    public String getCredentialId() {
        return  getString(credentialIdProperty);
    }

    public TotpUserFactorProfile setCredentialId(String credentialId) {
        setProperty(credentialIdProperty, credentialId);
        return this;
    }

}
