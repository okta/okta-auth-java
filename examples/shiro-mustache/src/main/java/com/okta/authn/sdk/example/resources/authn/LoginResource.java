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

import com.okta.authn.sdk.AuthenticationException;
import com.okta.authn.sdk.AuthenticationFailureException;
import com.okta.authn.sdk.Client;
import com.okta.authn.sdk.example.OktaStateHandler;
import com.okta.authn.sdk.example.models.authn.Factor;
import com.okta.authn.sdk.example.views.authn.ChangePasswordView;
import com.okta.authn.sdk.example.views.authn.LoginView;
import com.okta.authn.sdk.example.views.authn.MfaRequiredView;
import com.okta.authn.sdk.example.views.authn.MfaVerifyView;
import com.okta.authn.sdk.resource.AuthNRequest;
import com.okta.authn.sdk.resource.AuthNResult;

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

@Path("/login")
@Produces(MediaType.TEXT_HTML)
public class LoginResource {

    private final Client client;

    @Inject
    public LoginResource(Client client) {
        this.client = client;
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
    @Path("/mfa")
    public MfaRequiredView getRequireMfaView() {

        // grab previous AuthNStatus
        AuthNResult authNResult = getPreviousAuthResult();
        List<Factor> factors = authNResult.getFactors().stream()
            .map(authFactor -> {
                    String shortType = MfaVerifyView.relativeLink(authFactor);
                    return new Factor(authFactor.getId(),
                                               shortType,
                                               authFactor.getProvider(),
                                               authFactor.getVendorName(),
                                               authFactor.getProfile(),
                                               "/login/mfa/verify/" + shortType);
            })
            .collect(Collectors.toList());

        return new MfaRequiredView(factors);
    }

    @GET
    @Path("/mfa/verify/{type}")
    public MfaVerifyView getVerifyMfaView(@PathParam("type") String type) throws AuthenticationException {

        AuthNResult authNResult = getPreviousAuthResult();
        com.okta.authn.sdk.resource.Factor factor = getFactor(type, authNResult);

        if (factor.getType().equals("totp")) {
            return new MfaVerifyView(factor);
        } else {
            return challengeFactor(factor, authNResult);
        }
    }

    @POST
    public Response doLogin(@FormParam("username") String username,
                            @FormParam("password") String password) throws AuthenticationException {

        char[] pass = password != null ? password.toCharArray() : null;

        try {
            client.authenticate(username, pass, new OktaStateHandler());
            // the state handler will redirect
        } catch (AuthenticationFailureException e) {
            return Response.ok(new LoginView(Optional.of(e))).build();
        }
        return null;
    }

    @POST
    @Path("/change-password")
    public void changePassword(@FormParam("oldPassword") String oldPassword,
                               @FormParam("newPassword") String newPassword,
                               @Context HttpServletRequest request) throws AuthenticationException {

        client.changePassword(oldPassword.toCharArray(),
                              newPassword.toCharArray(),
                              getPreviousAuthResult().getStateToken(),
                  new OktaStateHandler());
    }

    @POST
    @Path("/mfa/verify/{type}")
    public void verifyMfaView(@PathParam("type") String type,
                              @FormParam("clientData") String clientData,
                              @FormParam("signatureData") String signatureData,
                              @FormParam("passCode") String passCode) throws AuthenticationException {

        AuthNResult previousAuthResult = getPreviousAuthResult();
        com.okta.authn.sdk.resource.Factor factor = getFactor(type, previousAuthResult);

        AuthNRequest request = client.fromResult(previousAuthResult)
                .setClientData(clientData)
                .setSignatureData(signatureData)
                .setPassCode(passCode);

        client.verifyFactor(factor, request, new OktaStateHandler());
    }


    private com.okta.authn.sdk.resource.Factor getFactor(String type, AuthNResult authNResult) {

        String oktaType = MfaVerifyView.fromRelativeLink(type);
        return authNResult.getFactors().stream()
                .filter(it -> it.getType().equals(oktaType))
                .findFirst().get();
    }

    private MfaVerifyView challengeFactor(com.okta.authn.sdk.resource.Factor factor, AuthNResult authNResult) throws AuthenticationException {

        ChallengeStateHandler handler = new ChallengeStateHandler();
        client.challengeFactor(factor, authNResult.getStateToken(), handler);

        AuthNResult challengeResult = handler.getChallengeResult();

        if (challengeResult == null) {
            throw new IllegalStateException("Challenge Result is empty, and other state was not triggered");
        }

        return new MfaVerifyView(challengeResult.getFactors().get(0)); // TODO: validate we only have one?
    }

    private AuthNResult getPreviousAuthResult() {
        return OktaStateHandler.getPreviousAuthResult();
    }

    private static class ChallengeStateHandler extends OktaStateHandler {

        private AuthNResult challengeResult;

        @Override
        public void handleMfaChallenge(AuthNResult recoveryChallenge) {
            challengeResult = recoveryChallenge;
        }

        AuthNResult getChallengeResult() {
            return challengeResult;
        }
    }
}