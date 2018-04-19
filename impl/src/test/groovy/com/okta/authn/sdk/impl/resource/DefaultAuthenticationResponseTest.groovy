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
package com.okta.authn.sdk.impl.resource

import com.okta.authn.sdk.impl.util.TestUtil
import com.okta.authn.sdk.resource.AuthenticationStatus
import com.okta.authn.sdk.resource.Factor
import com.okta.sdk.resource.user.factor.FactorProvider
import com.okta.sdk.resource.user.factor.FactorType
import org.testng.annotations.Test

import java.text.DateFormat
import java.text.SimpleDateFormat

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

class DefaultAuthenticationResponseTest {

    @Test
    void marshalTest() {

        def data = [
            type: "some_type",
            expiresAt: "2001-07-04T12:08:56.235-0700",
            status: "SUCCESS",
            factorResult: "WAITING",
            factorResultMessage: "factor-result-message",
            stateToken: "state-token",
            relayState: "relay-state",
            recoveryToken: "recovery-token",
            sessionToken: "session-token",
            idToken: "id-token",
            factorType: "factor-type",
            recoveryType: "recovery-type",
            _embedded: [
                user: [
                  id: "00ub0oNGTSWTBKOLGLNR",
                  passwordChanged: "2001-07-04T12:08:56.235-0700",
                  profile: [
                    login: "dade.murphy@example.com",
                    firstName: "Dade",
                    lastName: "Murphy",
                    locale: "en_US",
                    timeZone: "America/Los_Angeles"
                  ]
                ],
                factor: [
                  id: "opfh52xcuft3J4uZc0g3",
                  factorType: "push",
                  provider: "OKTA",
                  _embedded: [
                    activation: [
                      expiresAt: "2001-07-04T12:08:56.235-0700",
                      _links: [
                        qrcode: [
                          href: "https://dev-259824.oktapreview.com/api/v1/users/00ub0oNGTSWTBKOLGLNR/factors/opfh52xcuft3J4uZc0g3/qr/00fukNElRS_Tz6k-CFhg3pH4KO2dj2guhmaapXWbc4",
                          type: "image/png"
                        ],
                        send: [
                          [
                            name: "email",
                            href: "https://dev-259824.oktapreview.com/api/v1/users/00ub0oNGTSWTBKOLGLNR/factors/opfh52xcuft3J4uZc0g3/lifecycle/activate/email",
                            hints: [
                              allow: [
                                "POST"
                              ]
                            ]
                          ],
                          [
                            name: "sms",
                            href: "https://dev-259824.oktapreview.com/api/v1/users/00ub0oNGTSWTBKOLGLNR/factors/opfh52xcuft3J4uZc0g3/lifecycle/activate/sms",
                            hints: [
                              allow: [
                                "POST"
                              ]
                            ]
                          ]
                        ]
                      ]
                    ]
                  ]
                ]
            ],
            _links: [
                 next: [
                   name: "poll",
                   href: "https://dev-259824.oktapreview.com/api/v1/authn/factors/opfh52xcuft3J4uZc0g3/lifecycle/activate/poll",
                   hints: [
                     allow: [
                       "POST"
                     ]
                   ]
                 ],
                cancel: [
                  href: "https://dev-259824.oktapreview.com/api/v1/authn/cancel",
                  hints: [
                    allow: [
                      "POST"
                    ]
                  ]
                ],
                prev: [
                  href: "https://dev-259824.oktapreview.com/api/v1/authn/previous",
                  hints: [
                    allow: [
                      "POST"
                    ]
                  ]
                ]
              ]
        ]

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

        def response = new DefaultAuthenticationResponse(TestUtil.createMockDataStore(), data)
        assertThat response.getType(), is("some_type")
        assertThat response.getExpiresAt(), is(df.parse("2001-07-04T12:08:56.235-0700"))
        assertThat response.getStatus(), is(AuthenticationStatus.SUCCESS)
        assertThat response.getFactorResult(), is("WAITING")
        assertThat response.getFactorResultMessage(), is("factor-result-message")
        assertThat response.getStateToken(), is("state-token")
        assertThat response.getRelayState(), is("relay-state")
        assertThat response.getRecoveryToken(), is("recovery-token")
        assertThat response.getSessionToken(), is("session-token")
        assertThat response.getIdToken(), is("id-token")
        assertThat response.getFactorType(), is("factor-type")
        assertThat response.getRecoveryType(), is("recovery-type")

        assertThat response.getUser().getId(), is("00ub0oNGTSWTBKOLGLNR")
        assertThat response.getUser().getPasswordChanged(), is(df.parse("2001-07-04T12:08:56.235-0700"))
        assertThat response.getUser().getProfile(), equalTo([
                    login: "dade.murphy@example.com",
                    firstName: "Dade",
                    lastName: "Murphy",
                    locale: "en_US",
                    timeZone: "America/Los_Angeles"
                  ])
        assertThat response.getUser().getLogin(), is("dade.murphy@example.com")
        assertThat response.getUser().getFirstName(), is("Dade")
        assertThat response.getUser().getLastName(), is("Murphy")

        assertThat response.getUser().getLocale(), is(Locale.US)
        assertThat response.getUser().getTimeZone(), is(TimeZone.getTimeZone("America/Los_Angeles"))

        Factor factor = response.getFactors().get(0)
        assertThat factor.id, is("opfh52xcuft3J4uZc0g3")
        assertThat factor.type, is(FactorType.PUSH)
        assertThat factor.provider, is(FactorProvider.OKTA)
        assertThat factor.embedded, notNullValue(Map)
    }
}