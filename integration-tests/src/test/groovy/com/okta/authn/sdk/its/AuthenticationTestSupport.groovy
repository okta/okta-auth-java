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

import com.okta.authn.sdk.AuthenticationStateHandlerAdapter
import com.okta.authn.sdk.client.AuthenticationClient
import com.okta.authn.sdk.client.AuthenticationClients
import com.okta.authn.sdk.resource.AuthenticationResponse
import com.okta.sdk.client.Client
import com.okta.sdk.client.Clients
import com.okta.sdk.resource.Deletable
import com.okta.sdk.resource.user.User
import com.okta.sdk.resource.user.UserBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testng.annotations.AfterMethod
import org.testng.annotations.Listeners

@Listeners(ResourceExceptionLoggingListener)
class AuthenticationTestSupport {

    private Logger log = LoggerFactory.getLogger(AuthenticationClientIT)

    final static String SECURITY_QUESTION = "sec-question"
    final static String SECURITY_ANSWER = "sec-answer"
    final static char[] USER_PASSWORD = "Password1".toCharArray()

    def ignoringStateHandler = new IgnoringStateHandler()

    Client sdkClient = Clients.builder().build()
    AuthenticationClient authClient = AuthenticationClients.builder().build()

    private List<Deletable> toBeDeleted = []

    User randomUser(String email = null, String ... groups) {

        email = email ?: "joe.coder+" + UUID.randomUUID().toString() + "@example.com"

        Set<String> groupIds = new HashSet<>()
        if (groups) {
            groupIds.addAll(groups)
        }
        User user = UserBuilder.instance()
                .setEmail(email)
                .setFirstName("Joe")
                .setLastName("Code")
                .setPassword(USER_PASSWORD)
                .setActive(true)
                .setSecurityQuestion(SECURITY_QUESTION)
                .setSecurityQuestionAnswer(SECURITY_ANSWER)
                .setGroups(groupIds)
                .buildAndCreate(sdkClient)
        registerForCleanup(user)
        sleep(TestConfiguration.CONFIG.getAddUserDelay()) // work around any async server processing
        return user
    }

    /**
     * Registers a Deletable to be cleaned up after the test is run.
     * @param deletable Resource to be deleted.
     */
    void registerForCleanup(Deletable deletable) {
        toBeDeleted.add(deletable)
    }

    @AfterMethod
    void clean() {
        // delete them in reverse order so dependencies are resolved
        toBeDeleted.reverse().each { deletable ->
            try {
                if (deletable.metaClass.respondsTo(deletable, 'deactivate')) {
                    deletable.deactivate()
                }
                deletable.delete()
            }
            catch (Exception e) {
                log.trace("Exception thrown during cleanup, it is ignored so the rest of the cleanup can be run:", e)
            }
        }
    }

    static def ignoring = { Class<? extends Throwable> catchMe, Closure callMe ->
        try {
            callMe.call()
        } catch(e) {
            if (!e.class.isAssignableFrom(catchMe)) {
                throw e
            }
        }
    }
}

class IgnoringStateHandler extends AuthenticationStateHandlerAdapter {

    @Override
    void handleUnknown(AuthenticationResponse typedUnknownResponse) {}
}
