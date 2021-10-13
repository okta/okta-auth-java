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
import com.okta.authn.sdk.resource.FactorType;
import com.okta.example.helpers.AuthHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AuthenticationClient authenticationClient;

    @Autowired
    private AuthenticationStateHandler ignoringStateHandler;

    @RequestMapping("/login")
    public String handleGet() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView handlePost(final @RequestParam("username") String username,
                                   final @RequestParam("password") String password) {

        final ModelAndView modelAndView = new ModelAndView("home");
        AuthenticationResponse authenticationResponse;

        try {
            authenticationResponse = authenticationClient.authenticate(
                username, password.toCharArray(), null, ignoringStateHandler);

            // handle factors, if any
            if (authenticationResponse != null && !CollectionUtils.isEmpty(authenticationResponse.getFactors())) {
                return AuthHelper.proceedToVerifyView(authenticationResponse, authenticationClient, ignoringStateHandler);
            }
        } catch (final AuthenticationException e) {
            logger.error("Authentication Error - Status: {}, Code: {}, Message: {}",
                e.getStatus(), e.getCode(), e.getMessage());
            modelAndView.addObject("error",
                e.getStatus() + ":" + e.getCode() + ":" + e.getMessage());
            return modelAndView;
        }

        modelAndView.addObject("authenticationResponse", authenticationResponse);
        return modelAndView;
    }
}  