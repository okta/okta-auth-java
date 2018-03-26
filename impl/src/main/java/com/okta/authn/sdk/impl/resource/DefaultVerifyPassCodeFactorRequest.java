package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.VerifyPassCodeFactorRequest;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.BooleanProperty;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultVerifyPassCodeFactorRequest extends DefaultVerifyFactorRequest implements VerifyPassCodeFactorRequest {

    private static final StringProperty PASS_CODE_PROPERTY = new StringProperty("passCode");

    private static final BooleanProperty REMEMBER_DEVICE_PROPERTY = new BooleanProperty("rememberDevice");

    public DefaultVerifyPassCodeFactorRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultVerifyPassCodeFactorRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public String getPassCode() {
        return getString(PASS_CODE_PROPERTY);
    }

    @Override
    public VerifyPassCodeFactorRequest setPassCode(String passCode) {
        setProperty(PASS_CODE_PROPERTY, passCode);
        return this;
    }

    @Override
    public Boolean getRememberDevice() {
        return getNullableBoolean(REMEMBER_DEVICE_PROPERTY);
    }

    @Override
    public VerifyPassCodeFactorRequest setRememberDevice(Boolean rememberDevice) {
        setProperty(REMEMBER_DEVICE_PROPERTY, rememberDevice);
        return this;
    }
}
