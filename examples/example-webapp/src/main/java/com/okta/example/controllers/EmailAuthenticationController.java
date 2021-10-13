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
package com.okta.example.controllers;

import com.okta.authn.sdk.AuthenticationException;
import com.okta.authn.sdk.AuthenticationStateHandler;
import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.authn.sdk.resource.VerifyPassCodeFactorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EmailAuthenticationController {

    private final Logger logger = LoggerFactory.getLogger(EmailAuthenticationController.class);

    @Autowired
    private AuthenticationClient authenticationClient;

    @Autowired
    private AuthenticationStateHandler ignoringStateHandler;

    @RequestMapping("/verify-email-authenticator")
    public String handleGet() {
        return "verify-email-authenticator";
    }

    @RequestMapping(value = "/verify-email-authenticator", method = RequestMethod.POST)
    public ModelAndView handlePost(final @RequestParam(value = "passcode") String passcode,
                                   final @RequestParam(value = "factorId") String factorId,
                                   final @RequestParam(value = "stateToken") String stateToken) {

        final ModelAndView modelAndView = new ModelAndView("home");
        final AuthenticationResponse authenticationResponse;

        try {
            final VerifyPassCodeFactorRequest verifyPassCodeFactorRequest =
                authenticationClient.instantiate(VerifyPassCodeFactorRequest.class);
            verifyPassCodeFactorRequest.setStateToken(stateToken);
            verifyPassCodeFactorRequest.setPassCode(passcode);
            verifyPassCodeFactorRequest.setRememberDevice(false);

            authenticationResponse =
                authenticationClient.verifyFactor(factorId, verifyPassCodeFactorRequest, ignoringStateHandler);
        } catch (final AuthenticationException e) {
            logger.error("Verify Email Factor Error - Status: {}, Code: {}, Message: {}",
                e.getStatus(), e.getCode(), e.getMessage());
            final ModelAndView errorView = new ModelAndView();
            errorView.addObject("error",
                e.getStatus() + ":" + e.getCode() + ":" + e.getMessage());
            return errorView;
        }

        modelAndView.addObject("authenticationResponse", authenticationResponse);
        return modelAndView;
    }
}  