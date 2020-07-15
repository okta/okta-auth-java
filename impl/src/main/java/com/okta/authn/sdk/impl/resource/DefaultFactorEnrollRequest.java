/*
 * Copyright 2018-Present Okta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.FactorEnrollRequest;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.EnumProperty;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.ResourceReference;
import com.okta.sdk.impl.resource.StringProperty;
import com.okta.sdk.resource.user.factor.CallUserFactorProfile;
import com.okta.sdk.resource.user.factor.EmailUserFactorProfile;
import com.okta.sdk.resource.user.factor.FactorProvider;
import com.okta.sdk.resource.user.factor.FactorType;
import com.okta.sdk.resource.user.factor.HardwareUserFactorProfile;
import com.okta.sdk.resource.user.factor.PushUserFactorProfile;
import com.okta.sdk.resource.user.factor.SecurityQuestionUserFactorProfile;
import com.okta.sdk.resource.user.factor.SmsUserFactorProfile;
import com.okta.sdk.resource.user.factor.TokenUserFactorProfile;
import com.okta.sdk.resource.user.factor.TotpUserFactorProfile;
import com.okta.sdk.resource.user.factor.U2fUserFactorProfile;
import com.okta.sdk.resource.user.factor.WebAuthnUserFactorProfile;
import com.okta.sdk.resource.user.factor.WebUserFactorProfile;

import java.util.Map;

public class DefaultFactorEnrollRequest extends AbstractResource implements FactorEnrollRequest {

    private static final StringProperty STATE_TOKEN_PROPERTY = new StringProperty("stateToken");
    private static final EnumProperty<FactorType> FACTOR_TYPE_PROPERTY = new EnumProperty<>("factorType", FactorType.class);
    private static final ResourceReference<CallUserFactorProfile> CALL_USER_FACTOR_PROFILE_PROPERTY = new ResourceReference<>("profile", CallUserFactorProfile.class, true);
    private static final ResourceReference<EmailUserFactorProfile> EMAIL_USER_FACTOR_PROFILE_PROPERTY = new ResourceReference<>("profile", EmailUserFactorProfile.class, true);
    private static final ResourceReference<HardwareUserFactorProfile> HARDWARE_USER_FACTOR_PROFILE_PROPERTY = new ResourceReference<>("profile", HardwareUserFactorProfile.class, true);
    private static final ResourceReference<PushUserFactorProfile> PUSH_USER_FACTOR_PROFILE_PROPERTY = new ResourceReference<>("profile", PushUserFactorProfile.class, true);
    private static final ResourceReference<SecurityQuestionUserFactorProfile> SECURITY_USER_FACTOR_PROFILE_PROPERTY = new ResourceReference<>("profile", SecurityQuestionUserFactorProfile.class, true);
    private static final ResourceReference<SmsUserFactorProfile> SMS_USER_FACTOR_PROFILE_PROPERTY = new ResourceReference<>("profile", SmsUserFactorProfile.class, true);
    private static final ResourceReference<TokenUserFactorProfile> TOKEN_USER_FACTOR_PROFILE_PROPERTY = new ResourceReference<>("profile", TokenUserFactorProfile.class, true);
    private static final ResourceReference<TotpUserFactorProfile> TOTP_USER_FACTOR_PROFILE_PROPERTY = new ResourceReference<>("profile", TotpUserFactorProfile.class, true);
    private static final ResourceReference<U2fUserFactorProfile> U2F_USER_FACTOR_PROFILE_PROPERTY = new ResourceReference<>("profile", U2fUserFactorProfile.class, true);
    private static final ResourceReference<WebAuthnUserFactorProfile> WEB_AUTHN_USER_FACTOR_PROFILE_PROPERTY = new ResourceReference<>("profile", WebAuthnUserFactorProfile.class, true);
    private static final ResourceReference<WebUserFactorProfile> WEB_USER_FACTOR_PROFILE_PROPERTY = new ResourceReference<>("profile", WebUserFactorProfile.class, true);
    private static final EnumProperty<FactorProvider> PROVIDER_PROPERTY = new EnumProperty<>("provider", FactorProvider.class);

    public DefaultFactorEnrollRequest(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultFactorEnrollRequest(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(
            STATE_TOKEN_PROPERTY,
            FACTOR_TYPE_PROPERTY,
            CALL_USER_FACTOR_PROFILE_PROPERTY,
            EMAIL_USER_FACTOR_PROFILE_PROPERTY,
            HARDWARE_USER_FACTOR_PROFILE_PROPERTY,
            PUSH_USER_FACTOR_PROFILE_PROPERTY,
            SECURITY_USER_FACTOR_PROFILE_PROPERTY,
            SMS_USER_FACTOR_PROFILE_PROPERTY,
            TOKEN_USER_FACTOR_PROFILE_PROPERTY,
            TOTP_USER_FACTOR_PROFILE_PROPERTY,
            U2F_USER_FACTOR_PROFILE_PROPERTY,
            WEB_AUTHN_USER_FACTOR_PROFILE_PROPERTY,
            WEB_USER_FACTOR_PROFILE_PROPERTY,
            PROVIDER_PROPERTY);
    }

    @Override
    public String getStateToken() {
        return getString(STATE_TOKEN_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setStateToken(String stateToken) {
        setProperty(STATE_TOKEN_PROPERTY, stateToken);
        return this;
    }

    @Override
    public FactorType getFactorType() {
        return getEnumProperty(FACTOR_TYPE_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setFactorType(FactorType factorType) {
        setProperty(FACTOR_TYPE_PROPERTY, factorType);
        return this;
    }

    @Override
    public FactorProvider getProvider() {
        return  getEnumProperty(PROVIDER_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setProvider(FactorProvider provider) {
        setProperty(PROVIDER_PROPERTY, provider);
        return this;
    }

    @Override
    public CallUserFactorProfile getCallUserFactorProfile() {
        return getResourceProperty(CALL_USER_FACTOR_PROFILE_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setCallUserFactorProfile(CallUserFactorProfile callUserFactorProfile) {
        setProperty(CALL_USER_FACTOR_PROFILE_PROPERTY, callUserFactorProfile);
        return this;
    }

    @Override
    public EmailUserFactorProfile getEmailUserFactorProfile() {
        return getResourceProperty(EMAIL_USER_FACTOR_PROFILE_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setEmailUserFactorProfile(EmailUserFactorProfile emailUserFactorProfile) {
        setProperty(EMAIL_USER_FACTOR_PROFILE_PROPERTY, emailUserFactorProfile);
        return this;
    }

    @Override
    public HardwareUserFactorProfile getHardwareUserFactorProfile() {
        return getResourceProperty(HARDWARE_USER_FACTOR_PROFILE_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setHardwareUserFactorProfile(HardwareUserFactorProfile hardwareUserFactorProfile) {
        setProperty(HARDWARE_USER_FACTOR_PROFILE_PROPERTY, hardwareUserFactorProfile);
        return this;
    }

    @Override
    public PushUserFactorProfile getPushUserFactorProfile() {
        return getResourceProperty(PUSH_USER_FACTOR_PROFILE_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setPushUserFactorProfile(PushUserFactorProfile pushUserFactorProfile) {
        setProperty(PUSH_USER_FACTOR_PROFILE_PROPERTY, pushUserFactorProfile);
        return this;
    }

    @Override
    public SecurityQuestionUserFactorProfile getSecurityQuestionUserFactorProfile() {
        return getResourceProperty(SECURITY_USER_FACTOR_PROFILE_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setSecurityQuestionUserFactorProfile(SecurityQuestionUserFactorProfile securityQuestionUserFactorProfile) {
        setProperty(SECURITY_USER_FACTOR_PROFILE_PROPERTY, securityQuestionUserFactorProfile);
        return this;
    }

    @Override
    public SmsUserFactorProfile getSmsUserFactorProfile() {
        return getResourceProperty(SMS_USER_FACTOR_PROFILE_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setSmsUserFactorProfile(SmsUserFactorProfile smsUserFactorProfile) {
        setProperty(SMS_USER_FACTOR_PROFILE_PROPERTY, smsUserFactorProfile);
        return this;
    }

    @Override
    public TokenUserFactorProfile getTokenUserFactorProfile() {
        return getResourceProperty(TOKEN_USER_FACTOR_PROFILE_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setTokenUserFactorProfile(TokenUserFactorProfile tokenUserFactorProfile) {
        setProperty(TOKEN_USER_FACTOR_PROFILE_PROPERTY, tokenUserFactorProfile);
        return this;
    }

    @Override
    public TotpUserFactorProfile getTotpUserFactorProfile() {
        return getResourceProperty(TOTP_USER_FACTOR_PROFILE_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setTotpUserFactorProfile(TotpUserFactorProfile totpUserFactorProfile) {
        setProperty(TOTP_USER_FACTOR_PROFILE_PROPERTY, totpUserFactorProfile);
        return this;
    }

    @Override
    public U2fUserFactorProfile getU2fUserFactorProfile() {
        return getResourceProperty(U2F_USER_FACTOR_PROFILE_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setU2fUserFactorProfile(U2fUserFactorProfile u2fUserFactorProfile) {
        setProperty(U2F_USER_FACTOR_PROFILE_PROPERTY, u2fUserFactorProfile);
        return this;
    }

    @Override
    public WebAuthnUserFactorProfile getWebAuthnUserFactorProfile() {
        return getResourceProperty(WEB_AUTHN_USER_FACTOR_PROFILE_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setWebAuthnUserFactorProfile(WebAuthnUserFactorProfile webAuthnUserFactorProfile) {
        setProperty(WEB_AUTHN_USER_FACTOR_PROFILE_PROPERTY, webAuthnUserFactorProfile);
        return this;
    }

    @Override
    public WebUserFactorProfile getWebUserFactorProfile() {
        return getResourceProperty(WEB_USER_FACTOR_PROFILE_PROPERTY);
    }

    @Override
    public FactorEnrollRequest setWebUserFactorProfile(WebUserFactorProfile webUserFactorProfile) {
        setProperty(WEB_USER_FACTOR_PROFILE_PROPERTY, webUserFactorProfile);
        return this;
    }

}
