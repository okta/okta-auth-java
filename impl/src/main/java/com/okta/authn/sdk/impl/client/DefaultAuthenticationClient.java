/*
 * Copyright 2018-Present Okta, Inc.
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
package com.okta.authn.sdk.impl.client;

import com.okta.authn.sdk.AuthenticationException;
import com.okta.authn.sdk.AuthenticationFailureException;
import com.okta.authn.sdk.AuthenticationStateHandler;
import com.okta.authn.sdk.CredentialsException;
import com.okta.authn.sdk.FactorValidationException;
import com.okta.authn.sdk.InvalidAuthenticationStateException;
import com.okta.authn.sdk.InvalidRecoveryAnswerException;
import com.okta.authn.sdk.InvalidTokenException;
import com.okta.authn.sdk.InvalidUserException;
import com.okta.authn.sdk.http.Header;
import com.okta.authn.sdk.http.QueryParameter;
import com.okta.authn.sdk.http.RequestContext;
import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.resource.ActivateFactorRequest;
import com.okta.authn.sdk.resource.AuthenticationRequest;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.authn.sdk.resource.AuthenticationStatus;
import com.okta.authn.sdk.resource.ChallengeFactorRequest;
import com.okta.authn.sdk.resource.ChangePasswordRequest;
import com.okta.authn.sdk.resource.FactorEnrollRequest;
import com.okta.authn.sdk.resource.RecoverPasswordRequest;
import com.okta.authn.sdk.resource.RecoveryQuestionAnswerRequest;
import com.okta.authn.sdk.resource.StateTokenRequest;
import com.okta.authn.sdk.resource.UnlockAccountRequest;
import com.okta.authn.sdk.resource.VerifyFactorRequest;
import com.okta.authn.sdk.resource.VerifyRecoverTokenRequest;
import com.okta.authn.sdk.resource.VerifyRecoveryRequest;
import com.okta.sdk.cache.Caches;
import com.okta.sdk.impl.client.BaseClient;
import com.okta.sdk.impl.config.ClientConfiguration;
import com.okta.sdk.resource.Resource;
import com.okta.sdk.resource.ResourceException;
import com.okta.sdk.resource.user.factor.CallUserFactorProfile;
import com.okta.sdk.resource.user.factor.EmailUserFactorProfile;
import com.okta.sdk.resource.user.factor.HardwareUserFactorProfile;
import com.okta.sdk.resource.user.factor.PushUserFactorProfile;
import com.okta.sdk.resource.user.factor.SecurityQuestionUserFactorProfile;
import com.okta.sdk.resource.user.factor.SmsUserFactorProfile;
import com.okta.sdk.resource.user.factor.TokenUserFactorProfile;
import com.okta.sdk.resource.user.factor.TotpUserFactorProfile;
import com.okta.sdk.resource.user.factor.U2fUserFactorProfile;
import com.okta.sdk.resource.user.factor.UserFactorProfile;
import com.okta.sdk.resource.user.factor.FactorProvider;
import com.okta.sdk.resource.user.factor.FactorType;
import com.okta.sdk.resource.user.factor.WebAuthnUserFactorProfile;
import com.okta.sdk.resource.user.factor.WebUserFactorProfile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultAuthenticationClient extends BaseClient implements AuthenticationClient {

    /**
     * Instantiates a new AuthenticationClient instance that will communicate with the Okta REST API.  See the class-level
     * JavaDoc for a usage example.
     *
     * @param clientConfiguration      the {@link ClientConfiguration} containing the connection information
     */
    public DefaultAuthenticationClient(ClientConfiguration clientConfiguration) {
        super(clientConfiguration, Caches.newDisabledCacheManager());
    }

    @Override
    public AuthenticationResponse authenticate(String username, char[] password, String relayState, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return authenticate(instantiate(AuthenticationRequest.class)
                    .setUsername(username)
                    .setPassword(password)
                    .setRelayState(relayState),
                 stateHandler);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn", request, stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse changePassword(char[] oldPassword, char[] newPassword, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        // TODO: validate params? state is required, old and new will be validated on the server?
        return changePassword(instantiate(ChangePasswordRequest.class)
                    .setOldPassword(oldPassword)
                    .setNewPassword(newPassword)
                    .setStateToken(stateToken),
                stateHandler
        );
    }

    @Override
    public AuthenticationResponse changePassword(ChangePasswordRequest changePasswordRequest, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/credentials/change_password", changePasswordRequest, stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse resetPassword(char[] newPassword, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return resetPassword(instantiate(ChangePasswordRequest.class)
                                .setStateToken(stateToken)
                                .setNewPassword(newPassword),
                            stateHandler);
    }

    @Override
    public AuthenticationResponse resetPassword(ChangePasswordRequest request, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/credentials/reset_password", request, stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse recoverPassword(String username, FactorType factorType, String relayState, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return recoverPassword(instantiate(RecoverPasswordRequest.class)
                                    .setUsername(username)
                                    .setFactorType(factorType)
                                    .setRelayState(relayState),
                                stateHandler);
    }

    @Override
    public AuthenticationResponse recoverPassword(RecoverPasswordRequest request, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/recovery/password", request, stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse challengeFactor(String factorId, String stateToken, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        ChallengeFactorRequest request = instantiate(ChallengeFactorRequest.class)
                .setStateToken(stateToken);
        String href = "/api/v1/authn/factors/" + factorId + "/verify";
        return doPost(href, request, stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse verifyFactor(String factorId, String stateToken, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        // same request body as challengeFactor
        return challengeFactor(factorId, stateToken, requestContext, stateHandler);
    }

    @Override
    public AuthenticationResponse verifyFactor(String factorId, VerifyFactorRequest request, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/factors/" + factorId + "/verify", request, stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse activateFactor(String factorId, ActivateFactorRequest request, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/factors/" + factorId + "/lifecycle/activate", request, stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse verifyUnlockAccount(FactorType factorType, VerifyRecoveryRequest request, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/recovery/factors/" + factorType.name() + "/verify", request, stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse unlockAccount(String username, FactorType factorType, String relayState, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return unlockAccount(instantiate(UnlockAccountRequest.class)
                        .setUsername(username)
                        .setFactorType(factorType)
                        .setRelayState(relayState),
                    stateHandler);
    }

    @Override
    public AuthenticationResponse unlockAccount(UnlockAccountRequest request, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/recovery/unlock", request, stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse answerRecoveryQuestion(String answer, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return answerRecoveryQuestion(instantiate(RecoveryQuestionAnswerRequest.class)
                    .setAnswer(answer)
                    .setStateToken(stateToken),
                stateHandler);
    }

    @Override
    public AuthenticationResponse answerRecoveryQuestion(RecoveryQuestionAnswerRequest request, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/recovery/answer", request, stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse enrollFactor(FactorType type, FactorProvider provider, UserFactorProfile userFactorProfile, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        FactorEnrollRequest factorEnrollRequest = instantiate(FactorEnrollRequest.class)
            .setFactorType(type)
            .setProvider(provider)
            .setStateToken(stateToken);

        if (userFactorProfile instanceof CallUserFactorProfile) {
            factorEnrollRequest = factorEnrollRequest.setCallUserFactorProfile((CallUserFactorProfile) userFactorProfile);
        } else if (userFactorProfile instanceof EmailUserFactorProfile) {
            factorEnrollRequest = factorEnrollRequest.setEmailUserFactorProfile((EmailUserFactorProfile) userFactorProfile);
        } else if (userFactorProfile instanceof HardwareUserFactorProfile) {
            factorEnrollRequest = factorEnrollRequest.setHardwareUserFactorProfile((HardwareUserFactorProfile) userFactorProfile);
        } else if (userFactorProfile instanceof PushUserFactorProfile) {
            factorEnrollRequest = factorEnrollRequest.setPushUserFactorProfile((PushUserFactorProfile) userFactorProfile);
        } else if (userFactorProfile instanceof SecurityQuestionUserFactorProfile) {
            factorEnrollRequest = factorEnrollRequest.setSecurityQuestionUserFactorProfile((SecurityQuestionUserFactorProfile) userFactorProfile);
        } else if (userFactorProfile instanceof SmsUserFactorProfile) {
            factorEnrollRequest = factorEnrollRequest.setSmsUserFactorProfile((SmsUserFactorProfile) userFactorProfile);
        } else if (userFactorProfile instanceof TokenUserFactorProfile) {
            factorEnrollRequest = factorEnrollRequest.setTokenUserFactorProfile((TokenUserFactorProfile) userFactorProfile);
        } else if (userFactorProfile instanceof TotpUserFactorProfile) {
            factorEnrollRequest = factorEnrollRequest.setTotpUserFactorProfile((TotpUserFactorProfile) userFactorProfile);
        } else if (userFactorProfile instanceof U2fUserFactorProfile) {
            factorEnrollRequest = factorEnrollRequest.setU2fUserFactorProfile((U2fUserFactorProfile) userFactorProfile);
        } else if (userFactorProfile instanceof WebUserFactorProfile) {
            factorEnrollRequest = factorEnrollRequest.setWebUserFactorProfile((WebUserFactorProfile) userFactorProfile);
        } else if (userFactorProfile instanceof WebAuthnUserFactorProfile) {
            factorEnrollRequest = factorEnrollRequest.setWebAuthnUserFactorProfile((WebAuthnUserFactorProfile) userFactorProfile);
        }

        return enrollFactor(factorEnrollRequest, stateHandler);
    }

    @Override
    public AuthenticationResponse enrollFactor(FactorEnrollRequest request, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/factors", request, stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse previous(String stateToken, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/previous", toRequest(stateToken), stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse skip(String stateToken, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/skip", toRequest(stateToken), stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse cancel(String stateToken, RequestContext requestContext) throws AuthenticationException {
        return doPost("/api/v1/authn/cancel", toRequest(stateToken), null, requestContext);
    }

    @Override
    public AuthenticationResponse resendActivateFactor(String factorId, String stateToken, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/factors/" + factorId + "/lifecycle/resend", toRequest(stateToken), stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse resendVerifyFactor(String factorId, String stateToken, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/factors/" + factorId + "/verify/resend", toRequest(stateToken), stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse verifyActivation(String factorId, String stateToken, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/factors/" + factorId + "/lifecycle/activate/poll", toRequest(stateToken), stateHandler, requestContext);
    }

    @Override
    public AuthenticationResponse verifyRecoveryToken(String recoveryToken, RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        VerifyRecoverTokenRequest request = instantiate(VerifyRecoverTokenRequest.class)
                                                            .setRecoveryToken(recoveryToken);
        return doPost("/api/v1/authn/recovery/token", request, stateHandler, requestContext);
    }

    private StateTokenRequest toRequest(String stateToken) {
        return instantiate(StateTokenRequest.class)
                            .setStateToken(stateToken);
    }

    private void handleResult(AuthenticationResponse authenticationResponse, AuthenticationStateHandler authenticationStateHandler) {
        AuthenticationStatus status = authenticationResponse.getStatus();

         switch (status) {
             case SUCCESS:
                 authenticationStateHandler.handleSuccess(authenticationResponse);
                 break;
             case LOCKED_OUT:
                 authenticationStateHandler.handleLockedOut(authenticationResponse);
                 break;
             case MFA_CHALLENGE:
                 authenticationStateHandler.handleMfaChallenge(authenticationResponse);
                 break;
             case MFA_ENROLL:
                 authenticationStateHandler.handleMfaEnroll(authenticationResponse);
                 break;
             case MFA_ENROLL_ACTIVATE:
                 authenticationStateHandler.handleMfaEnrollActivate(authenticationResponse);
                 break;
             case MFA_REQUIRED:
                 authenticationStateHandler.handleMfaRequired(authenticationResponse);
                 break;
             case PASSWORD_EXPIRED:
                 authenticationStateHandler.handlePasswordExpired(authenticationResponse);
                 break;
             case PASSWORD_RESET:
                 authenticationStateHandler.handlePasswordReset(authenticationResponse);
                 break;
             case PASSWORD_WARN:
                 authenticationStateHandler.handlePasswordWarning(authenticationResponse);
                 break;
             case RECOVERY:
                 authenticationStateHandler.handleRecovery(authenticationResponse);
                 break;
             case RECOVERY_CHALLENGE:
                 authenticationStateHandler.handleRecoveryChallenge(authenticationResponse);
                 break;
             case UNAUTHENTICATED:
                 authenticationStateHandler.handleUnauthenticated(authenticationResponse);
                 break;
             default:
                 authenticationStateHandler.handleUnknown(authenticationResponse);
         }
    }

    private AuthenticationResponse doPost(String href, Resource request, AuthenticationStateHandler authenticationStateHandler, RequestContext requestContext) throws AuthenticationException {
        try {

            Map<String, Object> query = null;
            Map<String, List<String>> headers = null;

            if (requestContext != null) {
                query = requestContext.getQueryParams().stream()
                        .collect(Collectors.toMap(QueryParameter::getKey, QueryParameter::getValue));

                headers = requestContext.getHeaders().stream()
                        .collect(Collectors.toMap(Header::getKey, Header::getValue, (a, b) -> Stream.concat(a.stream(), b.stream()).collect(Collectors.toList())));
            }

            AuthenticationResponse authenticationResponse = getDataStore().create(href, request, null, AuthenticationResponse.class, query, headers);

            if (authenticationStateHandler != null) {
                handleResult(authenticationResponse, authenticationStateHandler);
            }

            return authenticationResponse;
        } catch (ResourceException e) {
            translateException(e);
            throw e; // above method should always throw
        }
    }

    private void translateException(ResourceException resourceException) throws AuthenticationException {

        String errorCode = resourceException.getCode();

        switch (errorCode) {
            case AuthenticationFailureException.ERROR_CODE:
                throw new AuthenticationFailureException(resourceException);

            case CredentialsException.ERROR_CODE:
                throw new CredentialsException(resourceException);

            case FactorValidationException.ERROR_CODE:
                throw new FactorValidationException(resourceException);

            case InvalidAuthenticationStateException.ERROR_CODE:
                throw new InvalidAuthenticationStateException(resourceException);

            case InvalidRecoveryAnswerException.ERROR_CODE:
                throw new InvalidRecoveryAnswerException(resourceException);

            case InvalidTokenException.ERROR_CODE:
                throw new InvalidTokenException(resourceException);

            case InvalidUserException.ERROR_CODE:
                throw new InvalidUserException(resourceException);

            default:
                throw resourceException;
        }
    }
}