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
package com.okta.authn.sdk.example;

import com.okta.authn.sdk.StateHandlerAdapter;
import com.okta.authn.sdk.resource.AuthNResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OktaStateHandler extends StateHandlerAdapter {

    private static final String PREVIOUS_AUTHN_RESULT = "previousAuthNResult";

    @Override
    public void handleSuccess(AuthNResult successResponse) {
        // the last request was a success, but if we do not have a session token
        // we need to force the flow to start over
        String relayState = successResponse.getRelayState();
        String dest = relayState != null ? relayState : "/troopers";
        redirect(dest, successResponse);
    }

    @Override
    public void handlePasswordExpired(AuthNResult passwordExpired) {
        redirect("/login/change-password", passwordExpired);
    }

    @Override
    public void handleMfaRequired(AuthNResult mfaRequiredResponse) {
        redirect("/login/mfa", mfaRequiredResponse);
    }

    @Override
    public void handleUnknown(AuthNResult unknownResponse) {
        redirect("/login?error="+ unknownResponse.getStatus().name(), unknownResponse);
    }

    private void redirect(String location, AuthNResult authNResult) {

        setResult(authNResult);
        try {
            Subject subject = SecurityUtils.getSubject();
            HttpServletResponse response = WebUtils.getHttpResponse(subject);
            setAuthNResult(authNResult);
            response.sendRedirect(location);
        } catch (IOException e) {
            throw new IllegalStateException("failed to redirect.", e);
        }
    }

    private static void setAuthNResult(AuthNResult authNResult) {
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().setAttribute(PREVIOUS_AUTHN_RESULT, authNResult);
    }

    public static AuthNResult getPreviousAuthResult() {
        Subject subject = SecurityUtils.getSubject();

        // TODO: use static key, or inject
        AuthNResult authNResult = (AuthNResult) subject.getSession().getAttribute(PREVIOUS_AUTHN_RESULT);
        System.out.println("authNResult: " + authNResult);

        return authNResult;
    }
}