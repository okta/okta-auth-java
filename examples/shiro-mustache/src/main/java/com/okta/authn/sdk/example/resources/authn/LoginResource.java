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
import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.example.ExampleAuthenticationStateHandler;
import com.okta.authn.sdk.example.models.authn.Factor;
import com.okta.authn.sdk.example.views.authn.ChangePasswordView;
import com.okta.authn.sdk.example.views.authn.LoginView;
import com.okta.authn.sdk.example.views.authn.MfaActivateView;
import com.okta.authn.sdk.example.views.authn.MfaEnrollSelectionView;
import com.okta.authn.sdk.example.views.authn.MfaEnrollView;
import com.okta.authn.sdk.example.views.authn.MfaRequiredView;
import com.okta.authn.sdk.example.views.authn.MfaVerifyView;
import com.okta.authn.sdk.example.views.authn.PasswordRecoveryView;
import com.okta.authn.sdk.example.views.authn.PasswordResetView;
import com.okta.authn.sdk.example.views.authn.RecoveryChallengeView;
import com.okta.authn.sdk.example.views.authn.RecoveryView;
import com.okta.authn.sdk.example.views.authn.UnlockAccountRecoveryView;
import com.okta.authn.sdk.example.views.authn.UnlockAccountView;
import com.okta.authn.sdk.resource.ActivateFactorRequest;
import com.okta.authn.sdk.resource.ActivatePassCodeFactorRequest;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.authn.sdk.resource.AuthenticationStatus;
import com.okta.authn.sdk.resource.FactorEnrollRequest;
import com.okta.authn.sdk.resource.VerifyFactorRequest;
import com.okta.authn.sdk.resource.VerifyPassCodeFactorRequest;
import com.okta.authn.sdk.resource.VerifyRecoveryRequest;
import com.okta.sdk.resource.user.factor.FactorProvider;
import com.okta.sdk.resource.user.factor.FactorType;
import com.okta.sdk.resource.user.factor.SmsFactorProfile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
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
    @Path("/reset")
    public PasswordResetView getPasswordResetView() {
        return new PasswordResetView();
    }

    @GET
    @Path("/unlock")
    public UnlockAccountView getUnlockAccountView() {
        return new UnlockAccountView();
    }

    @GET
    @Path("/unlock/recovery")
    public UnlockAccountRecoveryView getUnlockAccountRecoveryView() {
        return new UnlockAccountRecoveryView();
    }

    @GET
    @Path("/mfa/enroll")
    public MfaEnrollSelectionView getMfaEnrollSelectionView() {

        List<Factor> factors = getPreviousAuthResult().getFactors().stream()
            .map(authFactor -> {
                    String shortType = MfaVerifyView.relativeLink(authFactor);
                    return new Factor(authFactor.getId(),
                                      shortType,
                                      authFactor.getProvider().name(),
                                      authFactor.getVendorName(),
                                      authFactor.getProfile(),
                                      "/login/mfa/enroll/" + shortType);})
            .collect(Collectors.toList());
        return new MfaEnrollSelectionView(factors);
    }

    @GET
    @Path("/mfa/enroll/{factorType}")
    public MfaEnrollView getMfaEnrollView(@PathParam("factorType") String factorType) {
        return new MfaEnrollView(getFactor(factorType, getPreviousAuthResult()));
    }

    @GET
    @Path("/mfa/activate/{factorType}")
    public MfaActivateView getMfaActivateView() {
        return new MfaActivateView(getPreviousAuthResult().getFactors().get(0));
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
                                      authFactor.getProvider().name(),
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

        if (factor.getType().equals(FactorType.TOKEN_SOFTWARE_TOTP)) {
            return new MfaVerifyView(factor);
        } else {
            return new MfaVerifyView(challengeFactor(factor, authenticationResponse));
        }
    }

    @GET
    @Path("/mfa/resend/{type}")
    public MfaVerifyView getResendVerifyMfaView(@PathParam("type") String type) {

        AuthenticationResponse authenticationResponse = getPreviousAuthResult();
        com.okta.authn.sdk.resource.Factor factor = getFactor(type, authenticationResponse);
        return new MfaVerifyView(factor);
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
    @Path("/reset")
    public void resetPassword(@FormParam("newPassword") String newPassword) throws AuthenticationException {
        authenticationClient.resetPassword(newPassword.toCharArray(),
                                            getPreviousAuthResult().getStateToken(),
                                            new ExampleAuthenticationStateHandler());
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
        authenticationClient.recoverPassword(username, FactorType.valueOf(factorType), null, new ExampleAuthenticationStateHandler());
        return new RecoveryChallengeView();
    }

    @POST
    @Path("/unlock")
    public void unlockAccount(@FormParam("username") String username,
                              @FormParam("factor") String factorType) throws AuthenticationException {
        authenticationClient.unlockAccount(username, FactorType.valueOf(factorType), null, new ExampleAuthenticationStateHandler());
    }

    @POST
    @Path("/unlock/recovery")
    public void unlockAccountChallenge(@FormParam("passCode") String passCode) throws AuthenticationException {
        AuthenticationResponse previousAuthResult = getPreviousAuthResult();
        VerifyRecoveryRequest request = authenticationClient.instantiate(VerifyRecoveryRequest.class)
                .setStateToken(previousAuthResult.getStateToken())
                .setPassCode(passCode);
        authenticationClient.verifyUnlockAccount(previousAuthResult.getFactors().get(0).getType(), request, new ExampleAuthenticationStateHandler());
    }

    @GET
    @Path("/recovery")
    public RecoveryView getRecoveryView() {
        AuthenticationResponse previousAuthResult = getPreviousAuthResult();
        String question = previousAuthResult.getUser().getRecoveryQuestion().get("question");
        return new RecoveryView(question);
    }

    @POST
    @Path("/recovery")
    public void recoveryWithAnswer(@FormParam("answer") String answer) throws AuthenticationException {
        AuthenticationResponse previousAuthResult = getPreviousAuthResult();
        authenticationClient.answerRecoveryQuestion(answer,
                                                    previousAuthResult.getStateToken(),
                                                    new ExampleAuthenticationStateHandler());
    }

    @POST
    @Path("/recover/verify")
    public void recoverChallenge(@FormParam("passCode") String passCode) throws AuthenticationException {
        AuthenticationResponse previousAuthResult = getPreviousAuthResult();
        VerifyFactorRequest request = authenticationClient.instantiate(VerifyPassCodeFactorRequest.class)
                                                            .setPassCode(passCode)
                                                            .setStateToken(previousAuthResult.getStateToken());
        authenticationClient.verifyFactor(previousAuthResult.getFactors().get(0).getId(), request, new ExampleAuthenticationStateHandler());
    }

    @POST
    @Path("/mfa/verify/{type}")
    public void verifyMfa(@PathParam("type") String type,
                          @FormParam("clientData") String clientData,
                          @FormParam("signatureData") String signatureData,
                          @FormParam("passCode") String passCode) throws AuthenticationException {

        AuthenticationResponse previousAuthResult = getPreviousAuthResult();
        com.okta.authn.sdk.resource.Factor factor = getFactor(type, previousAuthResult);

        VerifyFactorRequest request = authenticationClient.instantiate(VerifyPassCodeFactorRequest.class)
                                                            .setPassCode(passCode)
                                                            .setStateToken(previousAuthResult.getStateToken());

        //TODO  this or the above method is likely wrong
        authenticationClient.verifyFactor(factor.getId(), request, new ExampleAuthenticationStateHandler());
    }

    @POST
    @Path("/mfa/resend/{type}")
    public MfaVerifyView verifyMfa(@PathParam("type") String type) throws AuthenticationException {

        AuthenticationResponse previousAuthResult = getPreviousAuthResult();
        com.okta.authn.sdk.resource.Factor factor = getFactor(type, previousAuthResult);
        AuthenticationResponse response = authenticationClient.resendVerifyFactor(factor.getId(), previousAuthResult.getStateToken(), new ExampleAuthenticationStateHandler());
        return new MfaVerifyView(factor);
    }

    @POST
    @Path("/mfa/activate/{factorType}")
    public void activateMfa(@FormParam("passCode") String passCode) throws AuthenticationException {

        AuthenticationResponse previousAuthResult = getPreviousAuthResult();
        String factorId = previousAuthResult.getFactors().get(0).getId();

        ActivateFactorRequest request = authenticationClient.instantiate(ActivatePassCodeFactorRequest.class)
                        .setPassCode(passCode)
                        .setStateToken(previousAuthResult.getStateToken());

        authenticationClient.activateFactor(factorId, request, new ExampleAuthenticationStateHandler());
    }

    @POST
    @Path("/mfa/activate/{factorType}/resend")
    public void resendActivateMfa(@PathParam("factorType") String type) throws AuthenticationException {

        AuthenticationResponse previousAuthResult = getPreviousAuthResult();
        com.okta.authn.sdk.resource.Factor factor = getFactor(type, previousAuthResult);
        authenticationClient.resendActivateFactor(factor.getId(), previousAuthResult.getStateToken(), new ExampleAuthenticationStateHandler());
    }

    @POST
    @Path("/mfa/enroll/{factorType}")
    public void enrollMfa(@PathParam("factorType") String factorType, Form form) throws AuthenticationException {
        FactorEnrollRequest request = authenticationClient.instantiate(FactorEnrollRequest.class)
                .setProvider(FactorProvider.OKTA)
                .setStateToken(getPreviousAuthResult().getStateToken())
                .setFactorType(MfaVerifyView.fromRelativeLink(factorType))
                .setFactorProfile(authenticationClient.instantiate(SmsFactorProfile.class)
                    .setPhoneNumber(form.asMap().getFirst("phoneNumber")));
        authenticationClient.enrollFactor(request, new ExampleAuthenticationStateHandler());
    }

    private com.okta.authn.sdk.resource.Factor getFactor(String type, AuthenticationResponse authenticationResponse) {

        FactorType oktaType = MfaVerifyView.fromRelativeLink(type);
        return authenticationResponse.getFactors().stream()
                .filter(it -> it.getType().equals(oktaType))
                .findFirst().get();
    }

    private com.okta.authn.sdk.resource.Factor challengeFactor(com.okta.authn.sdk.resource.Factor factor, AuthenticationResponse authenticationResponse) throws AuthenticationException {

        AuthenticationResponse challengeResult = authenticationClient.challengeFactor(factor.getId(), authenticationResponse.getStateToken(), new ExampleAuthenticationStateHandler());

        // check the response type
        if (!challengeResult.getStatus().equals(AuthenticationStatus.MFA_CHALLENGE)) {
            throw new IllegalStateException("Challenge Result is empty, and other state was not triggered");
        }

        return challengeResult.getFactors().get(0);
    }
}