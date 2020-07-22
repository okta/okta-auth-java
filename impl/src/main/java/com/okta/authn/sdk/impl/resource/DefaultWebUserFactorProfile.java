package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.WebUserFactorProfile;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultWebUserFactorProfile extends AbstractResource implements WebUserFactorProfile {

    private final static StringProperty credentialIdProperty = new StringProperty("credentialId");

    private final static Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(credentialIdProperty);

    public DefaultWebUserFactorProfile(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultWebUserFactorProfile(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return PROPERTY_DESCRIPTORS;
    }


    public String getCredentialId() {
        return  getString(credentialIdProperty);
    }

    public WebUserFactorProfile setCredentialId(String credentialId) {
        setProperty(credentialIdProperty, credentialId);
        return this;
    }

}
