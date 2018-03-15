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
package com.okta.authn.sdk.impl;

import com.okta.authn.sdk.AuthenticationException;
import com.okta.authn.sdk.AuthenticationFailureException;
import com.okta.authn.sdk.Client;
import com.okta.authn.sdk.CredentialsException;
import com.okta.authn.sdk.FactorValidationException;
import com.okta.authn.sdk.InvalidAuthenticationStateException;
import com.okta.authn.sdk.InvalidRecoveryAnswerException;
import com.okta.authn.sdk.InvalidTokenException;
import com.okta.authn.sdk.InvalidUserException;
import com.okta.authn.sdk.StateHandler;
import com.okta.authn.sdk.resource.AuthNRequest;
import com.okta.authn.sdk.resource.AuthNResult;
import com.okta.authn.sdk.resource.AuthNStatus;
import com.okta.authn.sdk.resource.ChangePasswordRequest;
import com.okta.authn.sdk.resource.Factor;
import com.okta.sdk.authc.credentials.ClientCredentials;
import com.okta.sdk.cache.CacheManager;
import com.okta.sdk.client.AuthenticationScheme;
import com.okta.sdk.client.Proxy;
import com.okta.sdk.impl.api.ClientCredentialsResolver;
import com.okta.sdk.impl.ds.DefaultDataStore;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.http.RequestExecutor;
import com.okta.sdk.impl.http.authc.RequestAuthenticatorFactory;
import com.okta.sdk.impl.util.BaseUrlResolver;
import com.okta.sdk.lang.Assert;
import com.okta.sdk.lang.Classes;
import com.okta.sdk.resource.Resource;
import com.okta.sdk.resource.ResourceException;

import java.lang.reflect.Constructor;

public class DefaultClient implements Client {

    private final InternalDataStore dataStore;

    /**
     * Instantiates a new Client instance that will communicate with the Okta REST API.  See the class-level
     * JavaDoc for a usage example.
     *
     * @param baseUrlResolver      Okta base URL resolver
     * @param proxy                the HTTP proxy to be used when communicating with the Okta API server (can be
     *                             null)
     * @param cacheManager         the {@link com.okta.sdk.cache.CacheManager} that should be used to cache
     *                             Okta REST resources (can be null)
     * @param authenticationScheme the HTTP authentication scheme to be used when communicating with the Okta API
     *                             server (can be null)
     * @param requestAuthenticatorFactory factory used to handle creating autentication requests
     * @param connectionTimeout    connection timeout in seconds
     */
    public DefaultClient(BaseUrlResolver baseUrlResolver, Proxy proxy, CacheManager cacheManager, AuthenticationScheme authenticationScheme, RequestAuthenticatorFactory requestAuthenticatorFactory, int connectionTimeout) {
        Assert.notNull(baseUrlResolver, "baseUrlResolver argument cannot be null.");
        Assert.isTrue(connectionTimeout >= 0, "connectionTimeout cannot be a negative number.");
        ClientCredentialsResolver clientCredentialsResolver = new DisabledClientCredentialsResolver();
        RequestExecutor requestExecutor = createRequestExecutor(clientCredentialsResolver.getClientCredentials(), proxy, authenticationScheme, requestAuthenticatorFactory, connectionTimeout);
        this.dataStore = createDataStore(requestExecutor, baseUrlResolver, clientCredentialsResolver, cacheManager);
    }


    protected InternalDataStore createDataStore(RequestExecutor requestExecutor, BaseUrlResolver baseUrlResolver, ClientCredentialsResolver clientCredentialsResolver, CacheManager cacheManager) {
        return new DefaultDataStore(requestExecutor, baseUrlResolver, clientCredentialsResolver, cacheManager);
    }

    @Override
    public ClientCredentials getClientCredentials() {
        return this.dataStore.getClientCredentials();
    }

    @Override
    public CacheManager getCacheManager() {
        return this.dataStore.getCacheManager();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected RequestExecutor createRequestExecutor(ClientCredentials clientCredentials, Proxy proxy, AuthenticationScheme authenticationScheme, RequestAuthenticatorFactory requestAuthenticatorFactory, int connectionTimeout) {

        String className = "com.okta.sdk.impl.http.httpclient.HttpClientRequestExecutor";

        Class requestExecutorClass;

        if (Classes.isAvailable(className)) {
            requestExecutorClass = Classes.forName(className);
        } else {
            //we might be able to check for other implementations in the future, but for now, we only support
            //HTTP calls via the HttpClient.  Throw an exception:

            String msg = "Unable to find the '" + className + "' implementation on the classpath.  Please ensure you " +
                    "have added the okta-sdk-httpclient .jar file to your runtime classpath.";
            throw new RuntimeException(msg);
        }

        Constructor<RequestExecutor> ctor = Classes.getConstructor(requestExecutorClass, ClientCredentials.class, Proxy.class, AuthenticationScheme.class, RequestAuthenticatorFactory.class, Integer.class);

        return Classes.instantiate(ctor, clientCredentials, proxy, authenticationScheme, requestAuthenticatorFactory, connectionTimeout);
    }

    /**
     * Delegates to the internal {@code dataStore} instance. This is a convenience mechanism to eliminate the constant
     * need to call {@code client.getDataStore()} every time one needs to instantiate Resource.
     *
     * @param clazz the Resource class to instantiate.
     * @param <T>   the Resource sub-type
     * @return a new instance of the specified Resource.
     */
    @Override
    public <T extends Resource> T instantiate(Class<T> clazz) {
        return this.dataStore.instantiate(clazz);
    }

    /**
     * Delegates to the internal {@code dataStore} instance. This is a convenience mechanism to eliminate the constant
     * need to call {@code client.getDataStore()} every time one needs to look up a Resource.
     *
     * @param href  the resource URL of the resource to retrieve
     * @param clazz the {@link Resource} sub-interface to instantiate
     * @param <T>   type parameter indicating the returned value is a {@link Resource} instance.
     * @return an instance of the specified class based on the data returned from the specified {@code href} URL.
     */
    @Override
    public <T extends Resource> T getResource(String href, Class<T> clazz) {
        return this.dataStore.getResource(href, clazz);
    }


    @Override
    public void authenticate(String username, char[] password, StateHandler stateHandler) throws AuthenticationException {
        authenticate(instantiate(AuthNRequest.class)
                .setUsername(username)
                .setPassword(password),
            stateHandler);
    }

    @Override
    public void authenticate(AuthNRequest request, StateHandler stateHandler) throws AuthenticationException {
        doPost("/api/v1/authn", request, stateHandler);
     }

    @Override
    public void changePassword(char[] oldPassword, char[] newPassword, String stateToken, StateHandler stateHandler) throws AuthenticationException {
        // TODO: validate params? state is required, old and new will be validated on the server?
        changePassword(instantiate(ChangePasswordRequest.class)
                .setOldPassword(oldPassword)
                .setNewPassword(newPassword)
                .setStateToken(stateToken),
            stateHandler
        );
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest, StateHandler stateHandler) throws AuthenticationException {
        doPost("/api/v1/authn/credentials/change_password", changePasswordRequest, stateHandler);
    }

    @Override
    public void challengeFactor(Factor factor, String stateToken, StateHandler stateHandler) throws AuthenticationException {
        AuthNRequest request = instantiate(AuthNRequest.class)
                .setStateToken(stateToken);

        String href = factor.getLinks()
                .get("verify")
                .getHref();

        doPost(href, request, stateHandler);
    }

    @Override
    public void verifyFactor(Factor factor, AuthNRequest request, StateHandler stateHandler) throws AuthenticationException {

        // TODO: i'm not sure this link lookup is valid for all factor types
        String href = factor.getLinks()
                .get("verify")
                .getHref();

        doPost(href, request, stateHandler);
    }

    private void handleResult(AuthNResult authNResult, StateHandler stateHandler) {
        AuthNStatus status = authNResult.getStatus();
        // TODO: add tests for getting as string then enum, then string again

         switch (status) {
             case SUCCESS:
                 stateHandler.handleSuccess(authNResult);
                 break;
             case LOCKED_OUT:
                 stateHandler.handleLockedOut(authNResult);
                 break;
             case MFA_CHALLENGE:
                 stateHandler.handleMfaChallenge(authNResult);
                 break;
             case MFA_ENROLL:
                 stateHandler.handleMfaEnroll(authNResult);
                 break;
             case MFA_ENROLL_ACTIVATE:
                 stateHandler.handleMfaEnrollActivate(authNResult);
                 break;
             case MFA_REQUIRED:
                 stateHandler.handleMfaRequired(authNResult);
                 break;
             case PASSWORD_EXPIRED:
                 stateHandler.handlePasswordExpired(authNResult);
                 break;
             case PASSWORD_RESET:
                 stateHandler.handlePasswordReset(authNResult);
                 break;
             case PASSWORD_WARN:
                 stateHandler.handlePasswordWarning(authNResult);
                 break;
             case RECOVERY:
                 stateHandler.handleRecovery(authNResult);
                 break;
             case RECOVERY_CHALLENGE:
                 stateHandler.handleRecoveryChallenge(authNResult);
                 break;
             case UNAUTHENTICATED:
                 stateHandler.handleUnauthenticated(authNResult);
                 break;
             default:
                 stateHandler.handleUnknown(authNResult);
         }
    }

    private void doPost(String href, Resource request, StateHandler stateHandler) throws AuthenticationException {
        try {
            AuthNResult authNResult = dataStore.create(href, request, AuthNResult.class);
            handleResult(authNResult, stateHandler);
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

    private static class DisabledClientCredentialsResolver implements ClientCredentialsResolver {

        private final ClientCredentials clientCredentials = new DisabledClientCredentials();

        @Override
        public ClientCredentials getClientCredentials() {
            return clientCredentials;
        }
    }

    private static class DisabledClientCredentials implements ClientCredentials {

        @Override
        public Object getCredentials() {
            return null;
        }
    }
}