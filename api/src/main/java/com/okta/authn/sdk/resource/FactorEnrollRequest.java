/*
 * Copyright 2018 Okta, Inc.
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
package com.okta.authn.sdk.resource;

import com.okta.sdk.resource.ExtensibleResource;
import com.okta.sdk.resource.user.factor.FactorProvider;
import com.okta.sdk.resource.user.factor.FactorType;
import com.okta.sdk.resource.user.factor.CallUserFactorProfile;
import com.okta.sdk.resource.user.factor.EmailUserFactorProfile;
import com.okta.sdk.resource.user.factor.HardwareUserFactorProfile;
import com.okta.sdk.resource.user.factor.PushUserFactorProfile;
import com.okta.sdk.resource.user.factor.SecurityQuestionUserFactorProfile;
import com.okta.sdk.resource.user.factor.SmsUserFactorProfile;
import com.okta.sdk.resource.user.factor.TokenUserFactorProfile;
import com.okta.sdk.resource.user.factor.TotpUserFactorProfile;
import com.okta.sdk.resource.user.factor.U2fUserFactorProfile;
import com.okta.sdk.resource.user.factor.WebAuthnUserFactorProfile;
import com.okta.sdk.resource.user.factor.WebUserFactorProfile;

public interface FactorEnrollRequest extends ExtensibleResource {

    String getStateToken();
    FactorEnrollRequest setStateToken(String stateToken);

    FactorType getFactorType();
    FactorEnrollRequest setFactorType(FactorType factorType);

    FactorProvider getProvider();
    FactorEnrollRequest setProvider(FactorProvider provider);

    CallUserFactorProfile getCallUserFactorProfile();
    FactorEnrollRequest setCallUserFactorProfile(CallUserFactorProfile callUserFactorProfile);

    EmailUserFactorProfile getEmailUserFactorProfile();
    FactorEnrollRequest setEmailUserFactorProfile(EmailUserFactorProfile emailUserFactorProfile);

    HardwareUserFactorProfile getHardwareUserFactorProfile();
    FactorEnrollRequest setHardwareUserFactorProfile(HardwareUserFactorProfile hardwareUserFactorProfile);

    PushUserFactorProfile getPushUserFactorProfile();
    FactorEnrollRequest setPushUserFactorProfile(PushUserFactorProfile pushUserFactorProfile);

    SecurityQuestionUserFactorProfile getSecurityQuestionUserFactorProfile();
    FactorEnrollRequest setSecurityQuestionUserFactorProfile(SecurityQuestionUserFactorProfile securityQuestionUserFactorProfile);

    SmsUserFactorProfile getSmsUserFactorProfile();
    FactorEnrollRequest setSmsUserFactorProfile(SmsUserFactorProfile smsUserFactorProfile);

    TokenUserFactorProfile getTokenUserFactorProfile();
    FactorEnrollRequest setTokenUserFactorProfile(TokenUserFactorProfile tokenUserFactorProfile);

    TotpUserFactorProfile getTotpUserFactorProfile();
    FactorEnrollRequest setTotpUserFactorProfile(TotpUserFactorProfile totpUserFactorProfile);

    U2fUserFactorProfile getU2fUserFactorProfile();
    FactorEnrollRequest setU2fUserFactorProfile(U2fUserFactorProfile u2fUserFactorProfile);

    WebAuthnUserFactorProfile getWebAuthnUserFactorProfile();
    FactorEnrollRequest setWebAuthnUserFactorProfile(WebAuthnUserFactorProfile webAuthnUserFactorProfile);

    WebUserFactorProfile getWebUserFactorProfile();
    FactorEnrollRequest setWebUserFactorProfile(WebUserFactorProfile webUserFactorProfile);
}