package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.StateTokenRequest;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultStateTokenRequest extends AbstractResource implements StateTokenRequest {

    private static final StringProperty STATE_TOKEN_PROPERTY = new StringProperty("stateToken");

    public DefaultStateTokenRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(STATE_TOKEN_PROPERTY);
    }

    @Override
    public String getStateToken() {
        return getString(STATE_TOKEN_PROPERTY);
    }

    @Override
    public StateTokenRequest setStateToken(String stateToken) {
        setProperty(STATE_TOKEN_PROPERTY, stateToken);
        return this;
    }
}