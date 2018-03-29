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
package com.okta.authn.sdk.its

import com.okta.authn.sdk.AuthenticationFailureException
import com.okta.authn.sdk.its.email.EmailClient
import com.okta.authn.sdk.resource.ActivatePassCodeFactorRequest
import com.okta.authn.sdk.resource.AuthenticationStatus
import com.okta.authn.sdk.resource.TotpFactorActivation
import com.okta.authn.sdk.resource.VerifyPassCodeFactorRequest
import com.okta.sdk.resource.user.factor.FactorProvider
import com.okta.sdk.resource.user.factor.FactorType
import com.okta.sdk.resource.user.factor.TotpFactorProfile
import org.jboss.aerogear.security.otp.Totp
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testng.TestException
import org.testng.annotations.Test

import java.util.regex.Pattern

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

class AuthenticationClientIT extends AuthentiationTestSupport {

    private Logger log = LoggerFactory.getLogger(AuthenticationClientIT)

    @Test
    void userLoginTest() {
        def user = randomUser()
        def response = authClient.authenticate(user.profile.getEmail(), USER_PASSWORD, null, ignoringStateHandler)
        assertThat response.status, is(AuthenticationStatus.SUCCESS)
        assertThat response.sessionToken, not(isEmptyString())
    }

    @Test
    void enrollFactorTest() {

        // first login to this group requires MFA enrollment
        def user = randomUser(null, TestConfiguration.CONFIG.mfaEnrollRequiredGroupId)

        def response1 = authClient.authenticate(user.profile.getEmail(), USER_PASSWORD, null, ignoringStateHandler)
        assertThat response1.status, is(AuthenticationStatus.MFA_ENROLL)
        assertThat response1.sessionToken, nullValue()

        // enroll in TOTP factor
        def totpFactor = authClient.instantiate(TotpFactorProfile)
                .setCredentialId(user.profile.getEmail())

        def response2 = authClient.enrollFactor(FactorType.TOKEN_SOFTWARE_TOTP,
                FactorProvider.OKTA,
                totpFactor,
                response1.stateToken,
                ignoringStateHandler)

        assertThat response2.status, is(AuthenticationStatus.MFA_ENROLL_ACTIVATE)
        assertThat response2.sessionToken, nullValue()

        // verify enrollment
        def factor = response2.getFactors().get(0)

        Totp totp = new Totp(factor.getActivation(TotpFactorActivation).sharedSecret)

        def totpActivateRequest = authClient.instantiate(ActivatePassCodeFactorRequest)
                .setPassCode(totp.now())
                .setStateToken(response2.stateToken)

        def response3 = authClient.activateFactor(factor.getId(), totpActivateRequest, ignoringStateHandler)
        assertThat response3.status, is(AuthenticationStatus.SUCCESS)
        assertThat response3.sessionToken, not(isEmptyString())

        // now login again and verify the factor on login
        def response4 = authClient.authenticate(user.profile.getEmail(), USER_PASSWORD, null, ignoringStateHandler)
        assertThat response4.status, is(AuthenticationStatus.MFA_REQUIRED)
        assertThat response4.sessionToken, nullValue()

        // force sleep to get next token
        sleep(30000l)

        def factorId = response4.factors.get(0).getId()
        def factorVerifyRequest = authClient.instantiate(VerifyPassCodeFactorRequest)
                                                            .setPassCode(totp.now())
                                                            .setStateToken(response4.stateToken)

        def response5 = authClient.verifyFactor(factorId, factorVerifyRequest, ignoringStateHandler)
        assertThat response5.status, is(AuthenticationStatus.SUCCESS)
        assertThat response5.sessionToken, not(isEmptyString())
    }

    @Test
    void forgotPasswordTest() {

        def emailClient = new EmailClient()
        def user = randomUser(emailClient.emailAddress)

        // login once
        def response1 = authClient.authenticate(user.profile.getEmail(), USER_PASSWORD, null, ignoringStateHandler)
        assertThat response1.status, is(AuthenticationStatus.SUCCESS)
        assertThat response1.sessionToken, not(isEmptyString())

        // try forget password flow
        def response2 = authClient.recoverPassword(user.profile.getEmail(), FactorType.EMAIL, null, ignoringStateHandler)
        assertThat response2.status, is(AuthenticationStatus.RECOVERY_CHALLENGE)
        assertThat response2.sessionToken, not(isEmptyString())
        String id = getRecoveryTokenFromEmail(emailClient)

        // exchange recovery token for state token
        def response3 = authClient.verifyRecoveryToken(id, ignoringStateHandler)
        assertThat response3.status, is(AuthenticationStatus.RECOVERY)
        assertThat response3.sessionToken, not(isEmptyString())
        assertThat response3.user, notNullValue()
        assertThat response3.user.recoveryQuestion, notNullValue()
        assertThat response3.user.recoveryQuestion.get("question"), is(SECURITY_QUESTION)

        // answer the security question
        def response4 = authClient.answerRecoveryQuestion(SECURITY_ANSWER, response3.stateToken, ignoringStateHandler)
        assertThat response4.status, is(AuthenticationStatus.PASSWORD_RESET)
        assertThat response4.sessionToken, nullValue()

        // change the password
        def newPassword = "Password2".toCharArray()
        def response5 = authClient.resetPassword(newPassword, response4.stateToken, ignoringStateHandler)
        assertThat response5.status, is(AuthenticationStatus.SUCCESS)
        assertThat response5.sessionToken, not(isEmptyString())

        // login again with new password
        def response6 = authClient.authenticate(user.profile.getEmail(), newPassword, null, ignoringStateHandler)
        assertThat response6.status, is(AuthenticationStatus.SUCCESS)
        assertThat response6.sessionToken, not(isEmptyString())
    }

    String getRecoveryTokenFromEmail(EmailClient emailClient) {
        String body = emailClient.recieveEmail()
        log.debug("Email body:\n" + body)

        def regex = "reset-password/([^\"]*)\">"
        def matcher = Pattern.compile(regex).matcher(body)
        if (matcher.find()) {
            String recoveryToken = matcher.group(1)
            log.debug("recoveryToken: ${recoveryToken}")
            return recoveryToken
        }
        throw new TestException("Could not find token in email")
    }

    @Test
    void passwordExpiredTest() {

        def user = randomUser()
        def tempPassword = user.expirePassword(true).tempPassword.toCharArray()

        def response1 = authClient.authenticate(user.profile.getEmail(), tempPassword, null, ignoringStateHandler)
        assertThat response1.status, is(AuthenticationStatus.PASSWORD_EXPIRED)
        assertThat response1.sessionToken, nullValue()

        // change the password
        def newPassword = "Password2".toCharArray()
        def response2 = authClient.changePassword(tempPassword, newPassword, response1.stateToken, ignoringStateHandler)
        assertThat response2.status, is(AuthenticationStatus.SUCCESS)
        assertThat response2.sessionToken, not(isEmptyString())

        // login again with new password
        def response3 = authClient.authenticate(user.profile.getEmail(), newPassword, null, ignoringStateHandler)
        assertThat response3.status, is(AuthenticationStatus.SUCCESS)
        assertThat response3.sessionToken, not(isEmptyString())
    }

    @Test
    void unlockAccountTest() {

        def emailClient = new EmailClient()
        def user = randomUser(emailClient.emailAddress)

        // force incorrect login to lock account
        def response1

        3.times {
            ignoring(AuthenticationFailureException) {
                response1 = authClient.authenticate(user.profile.getEmail(),
                                                    "wrong-password".toCharArray(),
                                                    null,
                                                    ignoringStateHandler)
            }
        }
        assertThat response1.status, is(AuthenticationStatus.LOCKED_OUT)
        assertThat response1.sessionToken, nullValue()

        def response2 = authClient.unlockAccount(user.profile.getEmail(), FactorType.EMAIL, null, ignoringStateHandler)
        assertThat response2.status, is(AuthenticationStatus.RECOVERY_CHALLENGE)
        assertThat response2.sessionToken, not(isEmptyString())
        String recoveryToken = getUnlockRecoveryTokenFromEmail(emailClient)

        // exchange recovery token for state token
        def response3 = authClient.verifyRecoveryToken(recoveryToken, ignoringStateHandler)
        assertThat response3.status, is(AuthenticationStatus.RECOVERY)
        assertThat response3.sessionToken, not(isEmptyString())
        assertThat response3.user, notNullValue()
        assertThat response3.user.recoveryQuestion, notNullValue()
        assertThat response3.user.recoveryQuestion.get("question"), is(SECURITY_QUESTION)

        // rest of flow tested above
    }

    String getUnlockRecoveryTokenFromEmail(EmailClient emailClient) {
        String body = emailClient.recieveEmail()
        log.debug("Email body:\n" + body)

        def regex = "user-unlock/([^\"]*)\">"
        def matcher = Pattern.compile(regex).matcher(body)
        if (matcher.find()) {
            String recoveryToken = matcher.group(1)
            log.debug("recoveryToken: ${recoveryToken}")
            return recoveryToken
        }
        throw new TestException("Could not find token in email")
    }

    @Test
    void cancelTest() {
        def relayState = "my-relay-state"
        // first login to this group requires MFA enrollment
        def user = randomUser(null, TestConfiguration.CONFIG.mfaEnrollRequiredGroupId)
        def response1 = authClient.authenticate(user.profile.getEmail(), USER_PASSWORD, relayState, ignoringStateHandler)

        assertThat response1.status, is(AuthenticationStatus.MFA_ENROLL)
        assertThat response1.sessionToken, nullValue()

        // cancel
        def response2 = authClient.cancel(response1.stateToken)
        assertThat response2.status, nullValue()
        assertThat response2.relayState, is(relayState)
    }

    @Test
    void previousStateTest() {
        // first login to this group requires MFA enrollment
        def user = randomUser(null, TestConfiguration.CONFIG.mfaEnrollRequiredGroupId)
        def response1 = authClient.authenticate(user.profile.getEmail(), USER_PASSWORD, null, ignoringStateHandler)

        assertThat response1.status, is(AuthenticationStatus.MFA_ENROLL)
        assertThat response1.sessionToken, nullValue()

        // enroll in TOTP factor
        def totpFactor = authClient.instantiate(TotpFactorProfile)
                .setCredentialId(user.profile.getEmail())

        def response2 = authClient.enrollFactor(FactorType.TOKEN_SOFTWARE_TOTP,
                FactorProvider.OKTA,
                totpFactor,
                response1.stateToken,
                ignoringStateHandler)

        assertThat response2.status, is(AuthenticationStatus.MFA_ENROLL_ACTIVATE)
        assertThat response2.sessionToken, nullValue()

        def response3 = authClient.previous(response2.stateToken, ignoringStateHandler)
        assertThat response3.status, is(AuthenticationStatus.MFA_ENROLL)
        assertThat response3.sessionToken, nullValue()
    }
}