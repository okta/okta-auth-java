package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.ActivateU2fFactorRequest;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultActivateU2fFactorRequest extends DefaultActivateFactorRequest implements ActivateU2fFactorRequest {


    private static final StringProperty REGISTRATION_DATA_PROPERTY = new StringProperty("registrationData");

    private static final StringProperty CLIENT_DATA_PROPERTY = new StringProperty("clientData");

    public DefaultActivateU2fFactorRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultActivateU2fFactorRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public String getRegistrationData() {
        return getString(REGISTRATION_DATA_PROPERTY);
    }

    @Override
    public ActivateU2fFactorRequest setRegistrationData(String registrationData) {
        setProperty(REGISTRATION_DATA_PROPERTY, registrationData);
        return this;
    }

    @Override
    public String getClientData() {
        return getString(CLIENT_DATA_PROPERTY);
    }

    @Override
    public ActivateU2fFactorRequest setClientData(String clientData) {
        setProperty(CLIENT_DATA_PROPERTY, clientData);
        return this;
    }
}
