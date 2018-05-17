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
package com.okta.authn.sdk.example.views.authn;

import com.okta.authn.sdk.example.views.BaseView;
import com.okta.authn.sdk.resource.Factor;
import com.okta.sdk.resource.user.factor.FactorType;

public class MfaVerifyView extends BaseView {

    private final Factor factor;

    public MfaVerifyView(Factor factor) {
        super("verify-" + relativeLink(factor) + ".mustache");
        this.factor = factor;
    }

    public Factor getFactor() {
        return factor;
    }

    public static String relativeLink(Factor factor) {
        FactorType type = factor.getType();
        String templateName;
        switch (type) {
            case U2F:
                templateName = "u2f";
                break;
            case TOKEN_SOFTWARE_TOTP:
                templateName = "totp";
                break;
            case SMS:
                templateName = "sms";
                break;
            default:
                templateName = "unknown";
        }

        return templateName;
    }

    public static FactorType fromRelativeLink(String type) {
        FactorType oktaType;
        switch (type) {
            case "u2f":
                oktaType = FactorType.U2F;
                break;
            case "totp":
                oktaType = FactorType.TOKEN_SOFTWARE_TOTP;
                break;
            case "sms":
                oktaType = FactorType.SMS;
                break;
            default:
                oktaType = null;
        }
        return oktaType;
    }
}