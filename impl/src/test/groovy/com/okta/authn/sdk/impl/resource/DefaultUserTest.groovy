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
import org.testng.annotations.Test

import java.text.DateFormat
import java.text.SimpleDateFormat

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.is

class DefaultUserTest {

    @Test
    void userLocalTest() {

        def data = [
              id: "00ub0oNGTSWTBKOLGLNR",
              passwordChanged: "2001-07-04T12:08:56.235-0700",
              profile: [
                login: "dade.murphy@example.com",
                firstName: "Dade",
                lastName: "Murphy",
                locale: "en_US",
                timeZone: "America/Los_Angeles"
              ]
            ]

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        def user = new DefaultUser(TestUtil.createMockDataStore(), data)

        assertThat user.id, is("00ub0oNGTSWTBKOLGLNR")
        assertThat user.passwordChanged, is(df.parse("2001-07-04T12:08:56.235-0700"))
        assertThat user.profile, equalTo([
                    login: "dade.murphy@example.com",
                    firstName: "Dade",
                    lastName: "Murphy",
                    locale: "en_US",
                    timeZone: "America/Los_Angeles"
                  ])
        assertThat user.login, is("dade.murphy@example.com")
        assertThat user.firstName, is("Dade")
        assertThat user.lastName, is("Murphy")
        assertThat user.timeZone, is(TimeZone.getTimeZone("America/Los_Angeles"))
        assertThat user.locale, is(Locale.US)
    }
}
