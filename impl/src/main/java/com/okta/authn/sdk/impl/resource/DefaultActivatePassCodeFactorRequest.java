package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.ActivatePassCodeFactorRequest;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultActivatePassCodeFactorRequest extends DefaultActivateFactorRequest implements ActivatePassCodeFactorRequest {

    private static final StringProperty PASS_CODE_PROPERTY = new StringProperty("passCode");

    public DefaultActivatePassCodeFactorRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultActivatePassCodeFactorRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public String getPassCode() {
        return getString(PASS_CODE_PROPERTY);
    }

    @Override
    public ActivatePassCodeFactorRequest setPassCode(String passCode) {
        setProperty(PASS_CODE_PROPERTY, passCode);
        return this;
    }
}
