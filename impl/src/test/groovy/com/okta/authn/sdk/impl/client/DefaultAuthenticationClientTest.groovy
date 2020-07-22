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
package com.okta.authn.sdk.impl.client

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.okta.authn.sdk.*
import com.okta.authn.sdk.http.RequestContext
import com.okta.authn.sdk.impl.util.TestUtil
import com.okta.authn.sdk.resource.ActivatePassCodeFactorRequest
import com.okta.authn.sdk.resource.AuthenticationRequest
import com.okta.authn.sdk.resource.AuthenticationResponse
import com.okta.authn.sdk.resource.AuthenticationStatus
import com.okta.authn.sdk.resource.CallUserFactorProfile
import com.okta.authn.sdk.resource.VerifyPassCodeFactorRequest
import com.okta.authn.sdk.resource.VerifyRecoveryRequest
import com.okta.sdk.client.AuthenticationScheme
import com.okta.sdk.impl.config.ClientConfiguration
import com.okta.commons.http.MediaType
import com.okta.commons.http.Request
import com.okta.commons.http.RequestExecutor
import com.okta.commons.http.DefaultResponse
import com.okta.sdk.impl.util.DefaultBaseUrlResolver
import com.okta.sdk.resource.ResourceException
import com.okta.sdk.resource.user.factor.FactorProvider
import com.okta.sdk.resource.user.factor.FactorType
import com.spotify.hamcrest.jackson.IsJsonObject
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.testng.annotations.Test

import static com.spotify.hamcrest.jackson.JsonMatchers.jsonObject
import static com.spotify.hamcrest.jackson.JsonMatchers.jsonText
import static org.hamcrest.Matchers.endsWith
import static org.hamcrest.Matchers.is
import static org.hamcrest.MatcherAssert.assertThat
import static org.mockito.Mockito.*

class DefaultAuthenticationClientTest {

    //@Test
    void authenticationSuccessTest() {

        def client = createClient("authenticationSuccessTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()

        requestExecutor.requestMatchers.add(bodyMatches(
            jsonObject()
                .where("username", is(jsonText("username1")))
                .where("password", is(jsonText("password2")))
        ))

        def stateHandler = mock(AuthenticationStateHandler)
        AuthenticationResponse response = client.authenticate("username1", "password2".toCharArray(), null, stateHandler)
        verify(stateHandler).handleSuccess(response)
    }

    @Test
    void changePasswordTest() {
        def client = createClient("changePasswordTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()

        requestExecutor.requestMatchers.add(bodyMatches(
            jsonObject()
                .where("stateToken",  is(jsonText("stateToken1")))
                .where("oldPassword", is(jsonText("oldPassword1")))
                .where("newPassword", is(jsonText("newPassword2")))
        ))

        def stateHandler = mock(AuthenticationStateHandler)
        AuthenticationResponse response = client.changePassword("oldPassword1".toCharArray(), "newPassword2".toCharArray(), "stateToken1", stateHandler)
        verify(stateHandler).handleSuccess(response)
    }

    @Test
    void resetPasswordTest() {
        def client = createClient("resetPasswordTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()

        requestExecutor.requestMatchers.add(bodyMatches(
                jsonObject()
                   .where("stateToken",  is(jsonText("stateToken1")))
                   .where("newPassword", is(jsonText("newPassword2")))
        ))

        def stateHandler = mock(AuthenticationStateHandler)
        AuthenticationResponse response = client.resetPassword("newPassword2".toCharArray(), "stateToken1", stateHandler)
        verify(stateHandler).handleSuccess(response)
    }

    @Test
    void enrollFactorTest() {
        def client = createClient("enrollFactorTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()

        requestExecutor.requestMatchers.add(
            bodyMatches(
                    jsonObject()
                        .where("stateToken",  is(jsonText("stateToken1")))
                        .where("factorType", is(jsonText("sms")))
                        .where("provider", is(jsonText("OKTA")))
                        .where("profile", is(jsonObject()
                            .where("phoneNumber", is(jsonText("555-555-1212")))))
        ))

        def factorProfile = client.instantiate(CallUserFactorProfile)
                                        .setPhoneNumber("555-555-1212")

        def stateHandler = mock(AuthenticationStateHandler)
        AuthenticationResponse response = client.enrollFactor(FactorType.SMS, FactorProvider.OKTA, factorProfile, "stateToken1", stateHandler)
        verify(stateHandler).handleMfaEnrollActivate(response)
    }

    @Test
    void activateFactorTest() {
        def client = createClient("activateFactorTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()
        String factorId = "factor321"

        requestExecutor.requestMatchers.add(bodyMatches(
            jsonObject()
                .where("stateToken", is(jsonText("stateToken1")))
                .where("passCode", is(jsonText("123456")))
        ))

        requestExecutor.requestMatchers.add(
            urlMatches(
                endsWith("/api/v1/authn/factors/${factorId}/lifecycle/activate")
        ))

        def request = client.instantiate(ActivatePassCodeFactorRequest)
                                        .setPassCode("123456")
                                        .setStateToken("stateToken1")

        def stateHandler = mock(AuthenticationStateHandler)
        AuthenticationResponse response = client.activateFactor(factorId, request, stateHandler)
        verify(stateHandler).handleSuccess(response)
    }

    @Test
    void verifyFactorTest() {
        def client = createClient("verifyFactorTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()
        String factorId = "factor321"

        requestExecutor.requestMatchers.add(bodyMatches(
            jsonObject()
                .where("stateToken", is(jsonText("stateToken1")))
                .where("passCode", is(jsonText("123456")))
        ))

        requestExecutor.requestMatchers.add(
            urlMatches(
                endsWith("/api/v1/authn/factors/${factorId}/verify")
        ))

        def request = client.instantiate(VerifyPassCodeFactorRequest)
                                        .setPassCode("123456")
                                        .setStateToken("stateToken1")

        def stateHandler = mock(AuthenticationStateHandler)
        AuthenticationResponse response = client.verifyFactor(factorId, request, stateHandler)
        verify(stateHandler).handleMfaChallenge(response)
    }

    @Test
    void activationPollTest() {
        def client = createClient("activationPollTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()
        String factorId = "factor321"

        requestExecutor.requestMatchers.add(bodyMatches(
            jsonObject()
                .where("stateToken", is(jsonText("stateToken1")))
        ))

        requestExecutor.requestMatchers.add(
            urlMatches(
                endsWith("/api/v1/authn/factors/${factorId}/lifecycle/activate/poll")
        ))

        def stateHandler = mock(AuthenticationStateHandler)
        AuthenticationResponse response = client.verifyActivation(factorId, "stateToken1", stateHandler)
        verify(stateHandler).handleMfaEnrollActivate(response)
        assertThat response.factorResult, is("WAITING")
    }

    @Test
    void skipStateTest() {
        def client = createClient("skipStateTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()

        requestExecutor.requestMatchers.add(bodyMatches(
            jsonObject()
                .where("stateToken", is(jsonText("stateToken1")))
        ))

        requestExecutor.requestMatchers.add(
            urlMatches(
                endsWith("/api/v1/authn/skip")
        ))

        def stateHandler = mock(AuthenticationStateHandler)
        AuthenticationResponse response = client.skip("stateToken1", stateHandler)
        verify(stateHandler).handleSuccess(response)
    }

    @Test
    void recoverPasswordTest() {
        def client = createClient("recoverPasswordTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()

        requestExecutor.requestMatchers.add(bodyMatches(
            jsonObject()
                .where("username", is(jsonText("joe.coder@example.com")))
                .where("relayState", is(jsonText("relayState1")))
                .where("factorType", is(jsonText("call")))
        ))

        requestExecutor.requestMatchers.add(
            urlMatches(
                endsWith("/api/v1/authn/recovery/password")
        ))

        def stateHandler = mock(AuthenticationStateHandler)
        AuthenticationResponse response = client.recoverPassword("joe.coder@example.com", FactorType.CALL, "relayState1", stateHandler)
        verify(stateHandler).handleRecoveryChallenge(response)
    }

    @Test
    void unlockAccountTest() {
        def client = createClient("unlockAccountTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()

        requestExecutor.requestMatchers.add(bodyMatches(
            jsonObject()
                .where("username", is(jsonText("joe.coder@example.com")))
                .where("relayState", is(jsonText("relayState1")))
                .where("factorType", is(jsonText("sms")))
        ))

        requestExecutor.requestMatchers.add(
            urlMatches(
                endsWith("/api/v1/authn/recovery/unlock")
        ))

        def stateHandler = mock(AuthenticationStateHandler)
        AuthenticationResponse response = client.unlockAccount("joe.coder@example.com", FactorType.SMS, "relayState1", stateHandler)
        verify(stateHandler).handleRecoveryChallenge(response)
    }

    @Test
    void answerRecoveryQuestionTest() {
        def client = createClient("answerRecoveryQuestionTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()

        requestExecutor.requestMatchers.add(bodyMatches(
            jsonObject()
                .where("stateToken", is(jsonText("stateToken1")))
                .where("answer", is(jsonText("answer goes here")))
        ))

        requestExecutor.requestMatchers.add(
            urlMatches(
                endsWith("/api/v1/authn/recovery/answer")
        ))

        def stateHandler = mock(AuthenticationStateHandler)
        AuthenticationResponse response = client.answerRecoveryQuestion("answer goes here", "stateToken1", stateHandler)
        verify(stateHandler).handlePasswordReset(response)
    }

    @Test
    void challengeFactorTest() {
        def client = createClient("challengeFactorTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()

        def factorId = "factor321"
        requestExecutor.requestMatchers.add(bodyMatches(
            jsonObject()
                .where("stateToken", is(jsonText("stateToken1")))
        ))

        requestExecutor.requestMatchers.add(
            urlMatches(
                endsWith("/api/v1/authn/factors/${factorId}/verify")
        ))

        def stateHandler = mock(AuthenticationStateHandler)
        AuthenticationResponse response = client.challengeFactor(factorId, "stateToken1", stateHandler)
        verify(stateHandler).handleMfaChallenge(response)
    }

    @Test
    void verifyFactorForStatusTest() {
        def client = createClient("verifyFactorForStatusTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()

        def factorId = "factor321"
        requestExecutor.requestMatchers.add(bodyMatches(
            jsonObject()
                .where("stateToken", is(jsonText("stateToken1")))
        ))

        requestExecutor.requestMatchers.add(
            urlMatches(
                endsWith("/api/v1/authn/factors/${factorId}/verify")
        ))

        def stateHandler = mock(AuthenticationStateHandler)
        AuthenticationResponse response = client.verifyFactor(factorId, "stateToken1", stateHandler)
        verify(stateHandler).handleMfaChallenge(response)
    }

    @Test
    void resendVerifyTest() {
        def client = createClient("resendVerifyTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()

        def factorId = "factor321"
        requestExecutor.requestMatchers.add(bodyMatches(
            jsonObject()
                .where("stateToken", is(jsonText("stateToken1")))
        ))

        requestExecutor.requestMatchers.add(
            urlMatches(
                endsWith("/api/v1/authn/factors/${factorId}/verify/resend")
        ))

        def stateHandler = mock(AuthenticationStateHandler)
        AuthenticationResponse response = client.resendVerifyFactor(factorId, "stateToken1", stateHandler)
        verify(stateHandler).handleMfaChallenge(response)
    }

    @Test
    void resendActivateTest() {
        def client = createClient("resendActivateTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()

        def factorId = "factor321"
        requestExecutor.requestMatchers.add(bodyMatches(
            jsonObject()
                .where("stateToken", is(jsonText("stateToken1")))
        ))

        requestExecutor.requestMatchers.add(
            urlMatches(
                endsWith("/api/v1/authn/factors/${factorId}/lifecycle/resend")
        ))

        def stateHandler = mock(AuthenticationStateHandler)
        AuthenticationResponse response = client.resendActivateFactor(factorId, "stateToken1", stateHandler)
        verify(stateHandler).handleMfaEnrollActivate(response)
    }

    @Test
    void verifyUnlockAccountTest() {
        def client = createClient("verifyUnlockAccountTest")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()

        requestExecutor.requestMatchers.add(bodyMatches(
            jsonObject()
                .where("stateToken", is(jsonText("stateToken1")))
                .where("passCode", is(jsonText("123654")))
        ))

        requestExecutor.requestMatchers.add(
            urlMatches(
                endsWith("/api/v1/authn/recovery/factors/SMS/verify")
        ))

        def stateHandler = mock(AuthenticationStateHandler)
        def request = client.instantiate(VerifyRecoveryRequest)
                                .setPassCode("123654")
                                .setStateToken("stateToken1")
        AuthenticationResponse response = client.verifyUnlockAccount(FactorType.SMS, request, stateHandler)
        verify(stateHandler).handleRecovery(response)
    }

    @Test
    void eachStatusTest() {

        def client = createClient("eachStatusTest")
        def stateHandler = mock(AuthenticationStateHandler)

        client.getRequestExecutor().interpolationData.put("status", AuthenticationStatus.SUCCESS)
        AuthenticationResponse response = client.authenticate("username1", "password2".toCharArray(), null, stateHandler)
        verify(stateHandler).handleSuccess(response)

        client.getRequestExecutor().interpolationData.put("status", AuthenticationStatus.UNAUTHENTICATED)
        response = client.authenticate("username1", "password2".toCharArray(), null, stateHandler)
        verify(stateHandler).handleUnauthenticated(response)

        client.getRequestExecutor().interpolationData.put("status", AuthenticationStatus.PASSWORD_WARN)
        response = client.authenticate("username1", "password2".toCharArray(), null, stateHandler)
        verify(stateHandler).handlePasswordWarning(response)

        client.getRequestExecutor().interpolationData.put("status", AuthenticationStatus.PASSWORD_EXPIRED)
        response = client.authenticate("username1", "password2".toCharArray(), null, stateHandler)
        verify(stateHandler).handlePasswordExpired(response)

        client.getRequestExecutor().interpolationData.put("status", AuthenticationStatus.RECOVERY)
        response = client.authenticate("username1", "password2".toCharArray(), null, stateHandler)
        verify(stateHandler).handleRecovery(response)

        client.getRequestExecutor().interpolationData.put("status", AuthenticationStatus.RECOVERY_CHALLENGE)
        response = client.authenticate("username1", "password2".toCharArray(), null, stateHandler)
        verify(stateHandler).handleRecoveryChallenge(response)

        client.getRequestExecutor().interpolationData.put("status", AuthenticationStatus.PASSWORD_RESET)
        response = client.authenticate("username1", "password2".toCharArray(), null, stateHandler)
        verify(stateHandler).handlePasswordReset(response)

        client.getRequestExecutor().interpolationData.put("status", AuthenticationStatus.LOCKED_OUT)
        response = client.authenticate("username1", "password2".toCharArray(), null, stateHandler)
        verify(stateHandler).handleLockedOut(response)

        client.getRequestExecutor().interpolationData.put("status", AuthenticationStatus.MFA_ENROLL)
        response = client.authenticate("username1", "password2".toCharArray(), null, stateHandler)
        verify(stateHandler).handleMfaEnroll(response)

        client.getRequestExecutor().interpolationData.put("status", AuthenticationStatus.MFA_ENROLL_ACTIVATE)
        response = client.authenticate("username1", "password2".toCharArray(), null, stateHandler)
        verify(stateHandler).handleMfaEnrollActivate(response)

        client.getRequestExecutor().interpolationData.put("status", AuthenticationStatus.MFA_REQUIRED)
        response = client.authenticate("username1", "password2".toCharArray(), null, stateHandler)
        verify(stateHandler).handleMfaRequired(response)

        client.getRequestExecutor().interpolationData.put("status", AuthenticationStatus.MFA_CHALLENGE)
        response = client.authenticate("username1", "password2".toCharArray(), null, stateHandler)
        verify(stateHandler).handleMfaChallenge(response)

        client.getRequestExecutor().interpolationData.put("status", AuthenticationStatus.UNKNOWN)
        response = client.authenticate("username1", "password2".toCharArray(), null, stateHandler)
        verify(stateHandler).handleUnknown(response)
    }

    @Test
    void eachErrorCodeTest() {

        def client = createClient("eachErrorCodeTest")
        verifyExceptionThrown(client, 401, AuthenticationFailureException.ERROR_CODE, AuthenticationFailureException)
        verifyExceptionThrown(client, 403, CredentialsException.ERROR_CODE, CredentialsException)
        verifyExceptionThrown(client, 403, FactorValidationException.ERROR_CODE, FactorValidationException)
        verifyExceptionThrown(client, 403, InvalidAuthenticationStateException.ERROR_CODE, InvalidAuthenticationStateException)
        verifyExceptionThrown(client, 403, InvalidRecoveryAnswerException.ERROR_CODE, InvalidRecoveryAnswerException)
        verifyExceptionThrown(client, 401, InvalidTokenException.ERROR_CODE, InvalidTokenException)
        verifyExceptionThrown(client, 403, InvalidUserException.ERROR_CODE, InvalidUserException)

        // other error
        verifyExceptionThrown(client, 444, "other-code", ResourceException)
    }

    @Test
    void requestContextIncluded() {
        def client = createClient("authenticationSuccess")
        StubRequestExecutor requestExecutor = client.getRequestExecutor()

        requestExecutor.requestMatchers.add(bodyMatches(
            jsonObject()
                .where("username", is(jsonText("username1")))
                .where("password", is(jsonText("password2")))
        ))
        requestExecutor.requestMatchers.add(headerMatches("header1", is(["hvalue1"])))
        requestExecutor.requestMatchers.add(headerMatches("header2", is(["hvalue2-a", "hvalue2-b"])))
        requestExecutor.requestMatchers.add(headerMatches("header3", is(["hvalue3-a", "hvalue3-b"])))
        requestExecutor.requestMatchers.add(queryParamMatches("query1", is("qvalue1")))

        def stateHandler = mock(AuthenticationStateHandler)
        def authRequest = client.instantiate(AuthenticationRequest)
                .setUsername("username1")
                .setPassword("password2".toCharArray())
        def requestContext = new RequestContext()
                .addHeader("header1", "hvalue1")
                .addHeader("header2", ["hvalue2-a", "hvalue2-b"])
                .addHeader("header3", "hvalue3-a")
                .addHeader("header3", "hvalue3-b")
                .addQuery("query1", "qvalue1")

        AuthenticationResponse response = client.authenticate(authRequest, requestContext, stateHandler)
        verify(stateHandler).handleSuccess(response)
    }

    def verifyExceptionThrown(def client, int httpStatus, String errorCode, Class<? extends Exception> exception) {

        def stateHandler = mock(AuthenticationStateHandler)
        def requestExecutor = mock(RequestExecutor)
        client.setRequestExecutor(requestExecutor)

        def responseText = """
            {
                "errorCode": "${errorCode}",
                "errorSummary": "Some error message",
                "errorLink": "a-link",
                "errorId": "en-error-id",
                "errorCauses": []
            }
        """

        TestUtil.expectException(exception) {
            def response = new DefaultResponse(httpStatus, MediaType.APPLICATION_JSON, new ByteArrayInputStream(responseText.bytes), responseText.length())
            when(requestExecutor.executeRequest(any(Request))).thenReturn(response)
            client.authenticate("wrong-username", "or-password".toCharArray(), null, stateHandler)
        }
        verifyZeroInteractions(stateHandler)
    }

    WrappedAuthenticationClient createClient(callingTestMethodName = Thread.currentThread().getStackTrace()[6].methodName) {
        def clientConfig = new ClientConfiguration()
        clientConfig.setBaseUrlResolver(new DefaultBaseUrlResolver("https://${getClass().name}/${callingTestMethodName}"))
        clientConfig.setAuthenticationScheme(AuthenticationScheme.NONE)
        clientConfig.setClientCredentialsResolver(new DisabledClientCredentialsResolver())

        return new WrappedAuthenticationClient(clientConfig)
    }

    // matchers
    static Matcher<Request> bodyMatches(final IsJsonObject matcher) {
        return new TypeSafeMatcher<Request>() {

            @Override
            protected boolean matchesSafely(Request item) {
                return matcher.matches(toJson(item))
            }

            @Override
            void describeTo(Description description) {
                description.appendText("body failed to match ")
                matcher.describeTo(description)
            }

            @Override
            protected void describeMismatchSafely(Request item, Description mismatchDescription) {
                matcher.describeMismatch(toJson(item), mismatchDescription)
            }

            private JsonNode toJson(Request item) {
                String jsonText = item.body.text
                item.body.reset()
                return new ObjectMapper().readTree(jsonText)
            }
        }
    }

    static Matcher<Request> urlMatches(final Matcher<String> matcher) {
        return new TypeSafeMatcher<Request>() {

            @Override
            protected boolean matchesSafely(Request item) {
                return matcher.matches(item.resourceUrl.toString())
            }

            @Override
            void describeTo(Description description) {
                description.appendText("Request URL failed to match ")
                matcher.describeTo(description)
            }

            @Override
            protected void describeMismatchSafely(Request item, Description mismatchDescription) {
                matcher.describeMismatch(item.resourceUrl.toString(), mismatchDescription)
            }
        }
    }

    static Matcher<Request> headerMatches(final String key, final Matcher<List<String>> matcher) {
        return new TypeSafeMatcher<Request>() {

            @Override
            protected boolean matchesSafely(Request item) {
                return matcher.matches(item.getHeaders().get(key))
            }

            @Override
            void describeTo(Description description) {
                description.appendText("Request Header failed to match ")
                matcher.describeTo(description)
            }

            @Override
            protected void describeMismatchSafely(Request item, Description mismatchDescription) {
                matcher.describeMismatch(item.getHeaders().get(key), mismatchDescription)
            }
        }
    }

    static Matcher<Request> queryParamMatches(final String key, final Matcher<String> matcher) {
        return new TypeSafeMatcher<Request>() {

            @Override
            protected boolean matchesSafely(Request item) {
                return matcher.matches(item.queryString.get(key))
            }

            @Override
            void describeTo(Description description) {
                description.appendText("Request Query Parms failed to match ")
                matcher.describeTo(description)
            }

            @Override
            protected void describeMismatchSafely(Request item, Description mismatchDescription) {
                matcher.describeMismatch(item.queryString.get(key), mismatchDescription)
            }
        }
    }
}
