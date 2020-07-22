package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.WebAuthnUserFactorProfile;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultWebAuthnUserFactorProfile extends AbstractResource implements WebAuthnUserFactorProfile {

    private final static StringProperty credentialIdProperty = new StringProperty("credentialId");
    private final static StringProperty authenticatorNameProperty = new StringProperty("authenticatorName");

    private final static Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(credentialIdProperty, authenticatorNameProperty);

    public DefaultWebAuthnUserFactorProfile(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultWebAuthnUserFactorProfile(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return PROPERTY_DESCRIPTORS;
    }


    public String getCredentialId() {
        return  getString(credentialIdProperty);
    }

    public WebAuthnUserFactorProfile setCredentialId(String credentialId) {
        setProperty(credentialIdProperty, credentialId);
        return this;
    }

    public String getAuthenticatorName() {
        return  getString(authenticatorNameProperty);
    }

    public WebAuthnUserFactorProfile setAuthenticatorName(String authenticatorName) {
        setProperty(authenticatorNameProperty, authenticatorName);
        return this;
    }

}
