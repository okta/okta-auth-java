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

import com.okta.authn.sdk.resource.Link
import com.okta.sdk.impl.ds.InternalDataStore
import org.testng.annotations.Test
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.aMapWithSize
import static org.hamcrest.Matchers.allOf
import static org.hamcrest.Matchers.contains
import static org.hamcrest.Matchers.hasEntry
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.instanceOf
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.nullValue
import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class LinkTest {

    @Test
    void nestedLinksTest() {

            def linksNode = [
              send: [
                [
                  name: "email",
                  href: "https://activate/email",
                  hints: [
                    allow: [
                      "POST"
                    ]
                  ]
                ],
                [
                  name: "sms",
                  href: "https://activate/sms",
                  hints: [
                    allow: [
                      "POST"
                    ]
                  ]
                ]
              ],
              qrcode: [
                href: "https://qrcode",
                type: "image/png"
              ]
            ]

        def dataStore = mock(InternalDataStore)
        when(dataStore.instantiate(eq(NestedLink), any(Map))).thenAnswer(new Answer<Object>() {
            @Override
            Link answer(InvocationOnMock invocation) throws Throwable {
                return new NestedLink(dataStore, invocation.arguments[1] as Map<String, Object>)
            }
        })
        when(dataStore.instantiate(eq(Link), any(Map))).thenAnswer(new Answer<Object>() {
            @Override
            Link answer(InvocationOnMock invocation) throws Throwable {
                return new DefaultLink(dataStore, invocation.arguments[1] as Map<String, Object>)
            }
        })

        def linkMap = DefaultLink.getLinks(linksNode, dataStore)
        assertThat linkMap, allOf(
                hasEntry(is("send"), instanceOf(NestedLink)),
                hasEntry(is("qrcode"), instanceOf(DefaultLink)),
                aMapWithSize(2))

        DefaultLink qrCodeLink = linkMap.get("qrcode")
        assertThat qrCodeLink.getName(), nullValue()
        assertThat qrCodeLink.getHref(), is("https://qrcode")
        assertThat qrCodeLink.getType(), is("image/png")
        assertThat qrCodeLink.getHintsAllow(), hasSize(0)
        assertThat qrCodeLink.hasNestedLinks(), is(false)
        assertThat qrCodeLink.getNestedLinks(), hasSize(0)

        NestedLink nestedLink = linkMap.get("send")
        assertThat nestedLink.getName(), nullValue()
        assertThat nestedLink.getHref(), nullValue()
        assertThat nestedLink.getType(), nullValue()
        assertThat nestedLink.getHintsAllow(), hasSize(0)
        assertThat nestedLink.hasNestedLinks(), is(true)
        assertThat nestedLink.getNestedLinks(), hasSize(2)

        Link emailLink = nestedLink.getNestedLinks().get(0)
        assertThat emailLink.getName(), is("email")
        assertThat emailLink.getHref(), is("https://activate/email")
        assertThat emailLink.getType(), nullValue()
        assertThat emailLink.getHintsAllow(), contains(is("POST"))
        assertThat emailLink.hasNestedLinks(), is(false)
        assertThat emailLink.getNestedLinks(), hasSize(0)

        Link smsLink = nestedLink.getNestedLinks().get(1)
        assertThat smsLink.getName(), is("sms")
        assertThat smsLink.getHref(), is("https://activate/sms")
        assertThat smsLink.getType(), nullValue()
        assertThat smsLink.getHintsAllow(), contains(is("POST"))
        assertThat smsLink.hasNestedLinks(), is(false)
        assertThat smsLink.getNestedLinks(), hasSize(0)
    }
}