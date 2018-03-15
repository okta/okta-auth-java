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

import com.okta.authn.sdk.resource.Factor;
import io.dropwizard.views.View;

public class MfaVerifyView extends View {

    private final Factor factor;

    public MfaVerifyView(Factor factor) {
        super("verify-" + relativeLink(factor) + ".mustache");
        this.factor = factor;
    }

    public Factor getFactor() {
        return factor;
    }

    public static String relativeLink(Factor factor) {
        String type = factor.getType();
        String templateName;
        switch (type) {
            case "u2f":
                templateName = "u2f";
                break;
            case "token:software:totp":
                templateName = "totp";
                break;
            case "sms":
                templateName = "sms";
                break;
            default:
                templateName = "unknown";
        }

        return templateName;
    }

    public static String fromRelativeLink(String type) {
        String oktaType;
        switch (type) {
            case "u2f":
                oktaType = "u2f";
                break;
            case "totp":
                oktaType = "token:software:totp";
                break;
            case "sms":
                oktaType = "sms";
                break;
            default:
                oktaType = "unknown";
        }
        return oktaType;
    }
}