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
import com.okta.sdk.cache.CacheManager;
import com.okta.sdk.cache.Caches;
import com.okta.sdk.client.AuthenticationScheme;
import com.okta.sdk.client.Proxy;
import com.okta.sdk.impl.client.BaseClient;
import com.okta.sdk.impl.config.ClientConfiguration;
import com.okta.sdk.impl.http.authc.RequestAuthenticatorFactory;
import com.okta.sdk.impl.util.BaseUrlResolver;
import com.okta.sdk.resource.Resource;
import com.okta.sdk.resource.ResourceException;
import com.okta.sdk.resource.user.factor.FactorProfile;
import com.okta.sdk.resource.user.factor.FactorProvider;
import com.okta.sdk.resource.user.factor.FactorType;

public class DefaultAuthenticationClient extends BaseClient implements AuthenticationClient {

    /**
     * Instantiates a new AuthenticationClient instance that will communicate with the Okta REST API.  See the class-level
     * JavaDoc for a usage example.
     *
     * @param baseUrlResolver      Okta base URL resolver
     * @param proxy                the HTTP proxy to be used when communicating with the Okta API server (can be
     *                             null)
     * @param cacheManager         the {@link com.okta.sdk.cache.CacheManager} that should be used to cache
     *                             Okta REST resources (can be null)
     * @param authenticationScheme the HTTP authentication scheme to be used when communicating with the Okta API
     *                             server (can be null)
     * @param requestAuthenticatorFactory factory used to handle creating authentication requests
     * @param connectionTimeout    connection timeout in seconds
     */
    @Deprecated
    public DefaultAuthenticationClient(BaseUrlResolver baseUrlResolver, Proxy proxy, CacheManager cacheManager, AuthenticationScheme authenticationScheme, RequestAuthenticatorFactory requestAuthenticatorFactory, int connectionTimeout) {
        super(new DisabledClientCredentialsResolver(), baseUrlResolver, proxy, cacheManager, authenticationScheme, requestAuthenticatorFactory, connectionTimeout);
    }

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
    public AuthenticationResponse authenticate(AuthenticationRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn", request, stateHandler);
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
    public AuthenticationResponse changePassword(ChangePasswordRequest changePasswordRequest, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/credentials/change_password", changePasswordRequest, stateHandler);
    }

    @Override
    public AuthenticationResponse resetPassword(char[] newPassword, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return resetPassword(instantiate(ChangePasswordRequest.class)
                                .setStateToken(stateToken)
                                .setNewPassword(newPassword),
                            stateHandler);
    }

    @Override
    public AuthenticationResponse resetPassword(ChangePasswordRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/credentials/reset_password", request, stateHandler);
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
    public AuthenticationResponse challengeFactor(String factorId, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        ChallengeFactorRequest request = instantiate(ChallengeFactorRequest.class)
                .setStateToken(stateToken);
        String href = "/api/v1/authn/factors/" + factorId + "/verify";
        return doPost(href, request, stateHandler);
    }

    @Override
    public AuthenticationResponse verifyFactor(String factorId, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        // same request body as challengeFactor
        return challengeFactor(factorId, stateToken, stateHandler);
    }

    @Override
    public AuthenticationResponse verifyFactor(String factorId, VerifyFactorRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/factors/" + factorId + "/verify", request, stateHandler);
    }

    @Override
    public AuthenticationResponse activateFactor(String factorId, ActivateFactorRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/factors/" + factorId + "/lifecycle/activate", request, stateHandler);
    }

    @Override
    public AuthenticationResponse recoverPassword(RecoverPasswordRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/recovery/password", request, stateHandler);
    }

    @Override
    public AuthenticationResponse verifyUnlockAccount(FactorType factorType, VerifyRecoveryRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/recovery/factors/" + factorType.name() + "/verify", request, stateHandler);
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
    public AuthenticationResponse unlockAccount(UnlockAccountRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/recovery/unlock", request, stateHandler);
    }

    @Override
    public AuthenticationResponse answerRecoveryQuestion(String answer, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return answerRecoveryQuestion(instantiate(RecoveryQuestionAnswerRequest.class)
                    .setAnswer(answer)
                    .setStateToken(stateToken),
                stateHandler);
    }

    @Override
    public AuthenticationResponse answerRecoveryQuestion(RecoveryQuestionAnswerRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/recovery/answer", request, stateHandler);
    }

    @Override
    public AuthenticationResponse enrollFactor(FactorType type, FactorProvider provider, FactorProfile profile, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return enrollFactor(instantiate(FactorEnrollRequest.class)
                                    .setFactorType(type)
                                    .setProvider(provider)
                                    .setFactorProfile(profile)
                                    .setStateToken(stateToken),
                                stateHandler);
    }

    @Override
    public AuthenticationResponse enrollFactor(FactorEnrollRequest request, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/factors", request, stateHandler);
    }

    @Override
    public AuthenticationResponse previous(String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/previous", toRequest(stateToken), stateHandler);
    }

    @Override
    public AuthenticationResponse skip(String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/skip", toRequest(stateToken), stateHandler);
    }

    @Override
    public AuthenticationResponse cancel(String stateToken) {
        return getDataStore().create("/api/v1/authn/cancel", toRequest(stateToken), AuthenticationResponse.class);
    }

    @Override
    public AuthenticationResponse resendActivateFactor(String factorId, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/factors/" + factorId + "/lifecycle/resend", toRequest(stateToken), stateHandler);
    }

    @Override
    public AuthenticationResponse resendVerifyFactor(String factorId, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/factors/" + factorId + "/verify/resend", toRequest(stateToken), stateHandler);
    }

    @Override
    public AuthenticationResponse verifyActivation(String factorId, String stateToken, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        return doPost("/api/v1/authn/factors/" + factorId + "/lifecycle/activate/poll", toRequest(stateToken), stateHandler);
    }

    @Override
    public AuthenticationResponse verifyRecoveryToken(String recoveryToken, AuthenticationStateHandler stateHandler) throws AuthenticationException {
        VerifyRecoverTokenRequest request = instantiate(VerifyRecoverTokenRequest.class)
                                                            .setRecoveryToken(recoveryToken);
        return doPost("/api/v1/authn/recovery/token", request, stateHandler);
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

    private AuthenticationResponse doPost(String href, Resource request, AuthenticationStateHandler authenticationStateHandler) throws AuthenticationException {
        try {
            AuthenticationResponse authenticationResponse = getDataStore().create(href, request, AuthenticationResponse.class);
            handleResult(authenticationResponse, authenticationStateHandler);
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