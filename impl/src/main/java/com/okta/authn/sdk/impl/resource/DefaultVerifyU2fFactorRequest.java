package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.ActivateU2fFactorRequest;
import com.okta.authn.sdk.resource.VerifyFactorRequest;
import com.okta.authn.sdk.resource.VerifyU2fFactorRequest;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultVerifyU2fFactorRequest extends DefaultVerifyFactorRequest implements VerifyU2fFactorRequest {


    private static final StringProperty SIGNATURE_DATA_PROPERTY = new StringProperty("signatureData");

    private static final StringProperty CLIENT_DATA_PROPERTY = new StringProperty("clientData");

    public DefaultVerifyU2fFactorRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultVerifyU2fFactorRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public String getClientData() {
        return getString(CLIENT_DATA_PROPERTY);
    }

    @Override
    public VerifyU2fFactorRequest setClientData(String clientData) {
        setProperty(CLIENT_DATA_PROPERTY, clientData);
        return this;
    }

    @Override
    public String getSignatureData() {
        return getString(SIGNATURE_DATA_PROPERTY);
    }

    @Override
    public VerifyFactorRequest setSignatureData(String signatureData) {
        setProperty(SIGNATURE_DATA_PROPERTY, signatureData);
        return this;
    }
}
