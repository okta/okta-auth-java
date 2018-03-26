package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.VerifyPushFactorRequest;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.BooleanProperty;

import java.util.Map;

public class DefaultVerifyPushFactorRequest extends DefaultVerifyFactorRequest implements VerifyPushFactorRequest {

    private static final BooleanProperty AUTO_PUSH_PROPERTY = new BooleanProperty("autoPush");

    private static final BooleanProperty REMEMBER_DEVICE_PROPERTY = new BooleanProperty("rememberDevice");

    public DefaultVerifyPushFactorRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultVerifyPushFactorRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Boolean getRememberDevice() {
        return getNullableBoolean(REMEMBER_DEVICE_PROPERTY);
    }

    @Override
    public VerifyPushFactorRequest setRememberDevice(Boolean rememberDevice) {
        setProperty(REMEMBER_DEVICE_PROPERTY, rememberDevice);
        return this;
    }

    @Override
    public Boolean getAutoPush() {
        return getNullableBoolean(AUTO_PUSH_PROPERTY);
    }

    @Override
    public VerifyPushFactorRequest setAutoPush(Boolean autoPush) {
        setProperty(AUTO_PUSH_PROPERTY, autoPush);
        return this;
    }
}
