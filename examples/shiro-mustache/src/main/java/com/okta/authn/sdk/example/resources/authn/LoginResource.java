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
package com.okta.authn.sdk.example.resources.authn;

import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.AuthenticationException;
import com.okta.authn.sdk.AuthenticationFailureException;
import com.okta.authn.sdk.example.ExampleAuthenticationStateHandler;
import com.okta.authn.sdk.example.models.authn.Factor;
import com.okta.authn.sdk.example.views.authn.ChangePasswordView;
import com.okta.authn.sdk.example.views.authn.LoginView;
import com.okta.authn.sdk.example.views.authn.MfaRequiredView;
import com.okta.authn.sdk.example.views.authn.MfaVerifyView;
import com.okta.authn.sdk.example.views.authn.PasswordRecoveryView;
import com.okta.authn.sdk.example.views.authn.RecoveryChallengeView;
import com.okta.authn.sdk.resource.AuthenticationRequest;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.authn.sdk.resource.AuthenticationStatus;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.okta.authn.sdk.example.ExampleAuthenticationStateHandler.getPreviousAuthResult;

@Path("/login")
@Produces(MediaType.TEXT_HTML)
public class LoginResource {

    private final AuthenticationClient authenticationClient;

    @Inject
    public LoginResource(AuthenticationClient authenticationClient) {
        this.authenticationClient = authenticationClient;
    }

    @GET
    public LoginView getLoginView(@Context HttpServletRequest request) {
        return new LoginView(Optional.empty());
    }

    @GET
    @Path("/change-password")
    public ChangePasswordView getChangePasswordView() {
        return new ChangePasswordView();
    }

    @GET
    @Path("/recover")
    public PasswordRecoveryView getPasswordRecoveryView() {
        return new PasswordRecoveryView();
    }

    @GET
    @Path("/mfa")
    public MfaRequiredView getRequireMfaView() {

        // grab previous AuthenticationStatus
        List<Factor> factors = getPreviousAuthResult().getFactors().stream()
            .map(authFactor -> {
                    String shortType = MfaVerifyView.relativeLink(authFactor);
                    return new Factor(authFactor.getId(),
                                      shortType,
                                      authFactor.getProvider(),
                                      authFactor.getVendorName(),
                                      authFactor.getProfile(),
                                      "/login/mfa/verify/" + shortType);})
            .collect(Collectors.toList());

        return new MfaRequiredView(factors);
    }

    @GET
    @Path("/mfa/verify/{type}")
    public MfaVerifyView getVerifyMfaView(@PathParam("type") String type) throws AuthenticationException {

        AuthenticationResponse authenticationResponse = getPreviousAuthResult();
        com.okta.authn.sdk.resource.Factor factor = getFactor(type, authenticationResponse);

        if (factor.getType().equals("totp")) {
            return new MfaVerifyView(factor);
        } else {
            return challengeFactor(factor, authenticationResponse);
        }
    }

    @POST
    public Response doLogin(@FormParam("username") String username,
                            @FormParam("password") String password) throws AuthenticationException {

        char[] pass = password != null ? password.toCharArray() : null;

        try {
            authenticationClient.authenticate(username, pass, null, new ExampleAuthenticationStateHandler());
            // the state handler will redirect
        } catch (AuthenticationFailureException e) {
            return Response.ok(new LoginView(Optional.of(e))).build();
        }
        return null;
    }

    @POST
    @Path("/change-password")
    public void changePassword(@FormParam("oldPassword") String oldPassword,
                               @FormParam("newPassword") String newPassword) throws AuthenticationException {

        authenticationClient.changePassword(oldPassword.toCharArray(),
                                            newPassword.toCharArray(),
                                            getPreviousAuthResult().getStateToken(),
                                            new ExampleAuthenticationStateHandler());
    }

    @POST
    @Path("/recover")
    public RecoveryChallengeView recoverPassword(@FormParam("username") String username,
                                                 @FormParam("factor") String factorType) throws AuthenticationException {
        authenticationClient.recoverPassword(username, factorType, null, new ExampleAuthenticationStateHandler());
        return new RecoveryChallengeView();
    }

    @POST
    @Path("/recover/verify")
    public void recoverChallenge(@FormParam("passCode") String passCode) throws AuthenticationException {
        AuthenticationResponse previousAuthResult = getPreviousAuthResult();
        AuthenticationRequest request = authenticationClient.fromResult(previousAuthResult)
                                                                .setPassCode(passCode);
        authenticationClient.verifyFactor(previousAuthResult.getFactors().get(0), request, new ExampleAuthenticationStateHandler());
    }

    @POST
    @Path("/mfa/verify/{type}")
    public void verifyMfaView(@PathParam("type") String type,
                              @FormParam("clientData") String clientData,
                              @FormParam("signatureData") String signatureData,
                              @FormParam("passCode") String passCode) throws AuthenticationException {

        AuthenticationResponse previousAuthResult = getPreviousAuthResult();
        com.okta.authn.sdk.resource.Factor factor = getFactor(type, previousAuthResult);

        AuthenticationRequest request = authenticationClient.fromResult(previousAuthResult)
                                                                .setClientData(clientData)
                                                                .setSignatureData(signatureData)
                                                                .setPassCode(passCode);

        authenticationClient.verifyFactor(factor, request, new ExampleAuthenticationStateHandler());
    }

    private com.okta.authn.sdk.resource.Factor getFactor(String type, AuthenticationResponse authenticationResponse) {

        String oktaType = MfaVerifyView.fromRelativeLink(type);
        return authenticationResponse.getFactors().stream()
                .filter(it -> it.getType().equals(oktaType))
                .findFirst().get();
    }

    private MfaVerifyView challengeFactor(com.okta.authn.sdk.resource.Factor factor, AuthenticationResponse authenticationResponse) throws AuthenticationException {

        AuthenticationResponse challengeResult = authenticationClient.challengeFactor(factor, authenticationResponse.getStateToken(), new ExampleAuthenticationStateHandler());

        // check the response type
        if (!challengeResult.getStatus().equals(AuthenticationStatus.MFA_CHALLENGE)) {
            throw new IllegalStateException("Challenge Result is empty, and other state was not triggered");
        }

        return new MfaVerifyView(challengeResult.getFactors().get(0)); // TODO: validate we only have one?
    }
}