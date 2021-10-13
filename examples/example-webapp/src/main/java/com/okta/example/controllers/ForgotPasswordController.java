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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class ForgotPasswordController {

    private final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    @Autowired
    private AuthenticationClient authenticationClient;

    @Autowired
    private AuthenticationStateHandler ignoringStateHandler;

    @RequestMapping("/forgot-password")
    public String handleForgotPasswordGet() {
        return "forgot-password";
    }

    @RequestMapping("/change-password")
    public String handleChangePasswordGet() {
        return "change-password";
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public ModelAndView handleForgotPasswordPost(final @RequestParam("email") String email) {

        final ModelAndView modelAndView = new ModelAndView("verify-recovery-token");

        AuthenticationResponse authenticationResponse;

        try {
            authenticationResponse = authenticationClient.recoverPassword(email, FactorType.EMAIL, null, ignoringStateHandler);
        } catch (final AuthenticationException e) {
            logger.error("Recover Password Error - Status: {}, Code: {}, Message: {}",
                e.getStatus(), e.getCode(), e.getMessage());
            modelAndView.addObject("error",
                e.getStatus() + ":" + e.getCode() + ":" + e.getMessage());
            return modelAndView;
        }

        logger.info("Recover Password Status: {}", authenticationResponse.getStatus());
        return modelAndView;
    }

    @RequestMapping(value = "/verify-recovery-token", method = RequestMethod.POST)
    public ModelAndView handleVerifyRecoveryTokenPost(final @RequestParam("recoveryToken") String recoveryToken) {

        final ModelAndView modelAndView = new ModelAndView("answer-sec-qn");
        final AuthenticationResponse authenticationResponse;

        try {
            authenticationResponse = authenticationClient.verifyRecoveryToken(recoveryToken,ignoringStateHandler);
        } catch (final AuthenticationException e) {
            logger.error("Verify Recovery Token Error - Status: {}, Code: {}, Message: {}",
                e.getStatus(), e.getCode(), e.getMessage());
            final ModelAndView errorView = new ModelAndView("verify-recovery-token");
            errorView.addObject("error",
                e.getStatus() + ":" + e.getCode() + ":" + e.getMessage());
            return errorView;
        }

        final String stateToken =  authenticationResponse.getStateToken();
        final String secQn = authenticationResponse.getUser().getRecoveryQuestion().get("question");

        logger.info("Verify Recovery Token Status: {}", authenticationResponse.getStatus());
        modelAndView.addObject("stateToken", stateToken);
        modelAndView.addObject("secQn", secQn);
        return modelAndView;
    }

    @RequestMapping(value = "/answer-sec-qn", method = RequestMethod.POST)
    public ModelAndView handleAnswerSecurityQuestionPost(final @RequestParam("secQnAnswer") String secQnAnswer,
                                                         final @RequestParam("stateToken") String stateToken) {

        final ModelAndView modelAndView = new ModelAndView("change-password");
        final AuthenticationResponse authenticationResponse;

        try {
            authenticationResponse = authenticationClient.answerRecoveryQuestion(secQnAnswer, stateToken, ignoringStateHandler);
        } catch (final AuthenticationException e) {
            logger.error("Answer Sec Qn Error - Status: {}, Code: {}, Message: {}",
                e.getStatus(), e.getCode(), e.getMessage());
            final ModelAndView errorView = new ModelAndView("change-password");
            errorView.addObject("error",
                e.getStatus() + ":" + e.getCode() + ":" + e.getMessage());
            return errorView;
        }

        logger.info("Answer Recovery Qn Status: {}", authenticationResponse.getStatus());
        modelAndView.addObject("stateToken", stateToken);
        return modelAndView;
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public ModelAndView handleChangePasswordPost(final @RequestParam("newPassword") String newPassword,
                                                 final @RequestParam("stateToken") String stateToken) {

        final ModelAndView modelAndView = new ModelAndView("home");
        final AuthenticationResponse authenticationResponse;

        try {
            authenticationResponse = authenticationClient.resetPassword(newPassword.toCharArray(), stateToken, ignoringStateHandler);
        } catch (final AuthenticationException e) {
            logger.error("Reset Password Error - Status: {}, Code: {}, Message: {}",
                e.getStatus(), e.getCode(), e.getMessage());
            final ModelAndView errorView = new ModelAndView("change-password");
            errorView.addObject("error",
                e.getStatus() + ":" + e.getCode() + ":" + e.getMessage());
            return errorView;
        }

        logger.info("Reset Password Status: {}", authenticationResponse.getStatus());
        modelAndView.addObject("authenticationResponse", authenticationResponse);
        return modelAndView;
    }

}  