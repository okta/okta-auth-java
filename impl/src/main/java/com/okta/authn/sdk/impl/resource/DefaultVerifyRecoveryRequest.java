package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.VerifyRecoveryRequest;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultVerifyRecoveryRequest extends AbstractResource implements VerifyRecoveryRequest {

    private static final StringProperty PASS_CODE_PROPERTY = new StringProperty("passCode");

    private static final StringProperty STATE_TOKEN_PROPERTY = new StringProperty("stateToken");

    public DefaultVerifyRecoveryRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultVerifyRecoveryRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return null;
    }

    @Override
    public String getStateToken() {
        return getString(STATE_TOKEN_PROPERTY);
    }

    @Override
    public VerifyRecoveryRequest setStateToken(String stateToken) {
        setProperty(STATE_TOKEN_PROPERTY, stateToken);
        return this;
    }

    @Override
    public String getPassCode() {
        return getString(PASS_CODE_PROPERTY);
    }

    @Override
    public VerifyRecoveryRequest setPassCode(String passCode) {
        setProperty(PASS_CODE_PROPERTY, passCode);
        return this;
    }
}
