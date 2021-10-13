/*
 * Copyright (c) 2021-Present, Okta, Inc.
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
package com.okta.example.helpers;

import com.okta.authn.sdk.AuthenticationException;
import com.okta.authn.sdk.AuthenticationStateHandler;
import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.authn.sdk.resource.FactorType;
import org.springframework.web.servlet.ModelAndView;

public class AuthHelper {

    public static ModelAndView proceedToVerifyView(final AuthenticationResponse authenticationResponse,
                                                   final AuthenticationClient authenticationClient,
                                                   final AuthenticationStateHandler ignoringStateHandler) throws AuthenticationException {

        final String factorId = authenticationResponse.getFactors().get(0).getId();
        final FactorType factorType = authenticationResponse.getFactors().get(0).getType();
        final String stateToken = authenticationResponse.getStateToken();

        // we only support Email factor for sample purpose
        if (factorType == FactorType.EMAIL) {
            AuthenticationResponse authResponse = authenticationClient.verifyFactor(factorId, stateToken, ignoringStateHandler);
            final ModelAndView emailAuthView = new ModelAndView("verify-email-authenticator");
            emailAuthView.addObject("authenticationResponse", authResponse);
            return emailAuthView;
        } else {
            final ModelAndView errorView = new ModelAndView("home");
            errorView.addObject("error", "Factor type " + factorType + " + is not supported in this sample yet");
            return errorView;
        }
    }

}
