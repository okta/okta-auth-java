package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.PushUserFactorProfile;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Map;

public class DefaultPushUserFactorProfile extends AbstractResource implements PushUserFactorProfile {

    private final static StringProperty credentialIdProperty = new StringProperty("credentialId");
    private final static StringProperty deviceTokenProperty = new StringProperty("deviceToken");
    private final static StringProperty deviceTypeProperty = new StringProperty("deviceType");
    private final static StringProperty nameProperty = new StringProperty("name");
    private final static StringProperty platformProperty = new StringProperty("platform");
    private final static StringProperty versionProperty = new StringProperty("version");

    private final static Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(credentialIdProperty, deviceTokenProperty, deviceTypeProperty, nameProperty, platformProperty, versionProperty);

    public DefaultPushUserFactorProfile(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultPushUserFactorProfile(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return PROPERTY_DESCRIPTORS;
    }


    public String getCredentialId() {
        return  getString(credentialIdProperty);
    }

    public PushUserFactorProfile setCredentialId(String credentialId) {
        setProperty(credentialIdProperty, credentialId);
        return this;
    }

    public String getDeviceToken() {
        return  getString(deviceTokenProperty);
    }

    public PushUserFactorProfile setDeviceToken(String deviceToken) {
        setProperty(deviceTokenProperty, deviceToken);
        return this;
    }

    public String getDeviceType() {
        return  getString(deviceTypeProperty);
    }

    public PushUserFactorProfile setDeviceType(String deviceType) {
        setProperty(deviceTypeProperty, deviceType);
        return this;
    }

    public String getName() {
        return  getString(nameProperty);
    }

    public PushUserFactorProfile setName(String name) {
        setProperty(nameProperty, name);
        return this;
    }

    public String getPlatform() {
        return  getString(platformProperty);
    }

    public PushUserFactorProfile setPlatform(String platform) {
        setProperty(platformProperty, platform);
        return this;
    }

    public String getVersion() {
        return  getString(versionProperty);
    }

    public PushUserFactorProfile setVersion(String version) {
        setProperty(versionProperty, version);
        return this;
    }

}
