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

import com.okta.authn.sdk.AuthenticationStateHandlerAdapter;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.sdk.impl.ds.DefaultResourceConverter;
import com.okta.sdk.impl.ds.JacksonMapMarshaller;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.ReferenceFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Locale;
import java.util.Map;

public class ExampleAuthenticationStateHandler extends AuthenticationStateHandlerAdapter {

    private static final String PREVIOUS_AUTHN_RESULT = "previousAuthNResult";

    @Override
    public void handleSuccess(AuthenticationResponse successResponse) {
        // the last request was a success, but if we do not have a session token
        // we need to force the flow to start over
        String relayState = successResponse.getRelayState();
        String dest = relayState != null ? relayState : "/troopers";
        redirect(dest, successResponse);
    }

    @Override
    public void handlePasswordExpired(AuthenticationResponse passwordExpired) {
        redirect("/login/change-password", passwordExpired);
    }

    @Override
    public void handleMfaRequired(AuthenticationResponse mfaRequiredResponse) {
        redirect("/login/mfa", mfaRequiredResponse);
    }

    @Override
    public void handleMfaChallenge(AuthenticationResponse mfaChallengeResponse) {
        // do nothing this will be handled by the caller
        String factorType = mfaChallengeResponse.getFactors().get(0).getType().name().toLowerCase(Locale.ENGLISH);
        redirect("/login/mfa/verify/"+ factorType, mfaChallengeResponse);
//        log(mfaChallengeResponse);
    }

    @Override
    public void handleLockedOut(AuthenticationResponse lockedOutResponse) {
        redirect("/login/unlock", lockedOutResponse);
    }

    @Override
    public void handleRecoveryChallenge(AuthenticationResponse recoveryChallenge) {
        redirect("/login/unlock/recovery", recoveryChallenge);
    }

    @Override
    public void handleRecovery(AuthenticationResponse recoveryResponse) {
        redirect("/login/recovery", recoveryResponse);
    }

    @Override
    public void handleMfaEnroll(AuthenticationResponse mfaEnroll) {
        redirect("/login/mfa/enroll", mfaEnroll);
    }

    @Override
    public void handleMfaEnrollActivate(AuthenticationResponse mfaEnrollActivate) {
        String factorType = mfaEnrollActivate.getFactors().get(0).getType().name().toLowerCase(Locale.ENGLISH);
        redirect("/login/mfa/activate/"+ factorType, mfaEnrollActivate);
    }

    @Override
    public void handlePasswordReset(AuthenticationResponse passwordReset) {
        redirect("/login/reset", passwordReset);
    }

    @Override
    public void handleUnknown(AuthenticationResponse unknownResponse) {
        redirect("/login?error="+ unknownResponse.getStatus().name(), unknownResponse);
    }

    private void log(AuthenticationResponse authenticationResponse) {
         String pid = ManagementFactory.getRuntimeMXBean().getName();
        File targetDir = new File("target/"+pid);
        targetDir.mkdirs();
        File outFile = new File(targetDir, "response-"+System.currentTimeMillis()+".json");

        ReferenceFactory referenceFactory = new ReferenceFactory();
        DefaultResourceConverter resourceConverter = new DefaultResourceConverter(referenceFactory);
        Map<String, Object> data = resourceConverter.convert((AbstractResource) authenticationResponse, false);

        JacksonMapMarshaller mapMarshaller = new JacksonMapMarshaller();
        try(FileOutputStream fos = new FileOutputStream(outFile)) {
            mapMarshaller.marshal(fos, data);
        } catch (IOException e) {
            throw new IllegalStateException("failed to serialize response.", e);
        }
    }

    private void redirect(String location, AuthenticationResponse authenticationResponse) {

        log(authenticationResponse);
        try {
            Subject subject = SecurityUtils.getSubject();
            HttpServletResponse response = WebUtils.getHttpResponse(subject);
            setAuthNResult(authenticationResponse);
            response.sendRedirect(location);
        } catch (IOException e) {
            throw new IllegalStateException("failed to redirect.", e);
        }
    }

    static void setAuthNResult(AuthenticationResponse authenticationResponse) {
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().setAttribute(PREVIOUS_AUTHN_RESULT, authenticationResponse);
    }

    public static AuthenticationResponse getPreviousAuthResult() {
        Subject subject = SecurityUtils.getSubject();

        // TODO: use static key, or inject
        AuthenticationResponse authenticationResponse = (AuthenticationResponse) subject.getSession().getAttribute(PREVIOUS_AUTHN_RESULT);
        System.out.println("authenticationResponse: " + authenticationResponse);

        return authenticationResponse;
    }
}