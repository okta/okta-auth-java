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
package com.okta.authn.sdk.its.email

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.testng.Assert

import static io.restassured.RestAssured.get

class EmailClient {

    private static final String GUERILLA_MAIL_BASE = "http://api.guerrillamail.com/ajax.php"
    private static final ObjectMapper mapper = new ObjectMapper()

    private final String sidToken
    final String emailAddress

    EmailClient() {
        def email = createEmailAccount()
        sidToken = email.token
        emailAddress = email.email
    }

    String getEmail(String fromAddressDomain) {
        String emailId = null
        int count = 0

        while (emailId == null && count++ < 200) {
            Thread.sleep(300l)
            def jsonResponse =
                    get(GUERILLA_MAIL_BASE + "?f=get_email_list&offset=0&sid_token=${sidToken}").asString()

            JsonNode emailList = null

            try {
                JsonNode rootNode = mapper.readTree(jsonResponse)
                emailList = rootNode.path("list")
            } catch (JsonMappingException e) {
                // gonna try to hit the api again, so ok to swallow exception
            }

            if (emailList != null) {
                for (JsonNode emailNode : emailList) {
                    String mailFrom = emailNode.get("mail_from").asText()
                    String localEmailId = emailNode.get("mail_id").asText()
                    if (mailFrom.contains(fromAddressDomain)) {
                        emailId = localEmailId
                        break
                    }
                }
            }
        }

        if (emailId == null) {
            Assert.fail("Couldn't retrieve email")
            return null
        }

        // fetch stormpath email content
        def jsonResponse = get(
                GUERILLA_MAIL_BASE + "?f=fetch_email&sid_token=${sidToken}&email_id=${emailId}"
        ).asString()
        def rootNode = mapper.readTree(jsonResponse)
        String emailBody = rootNode.get("mail_body").asText()

        return emailBody
    }

    String recieveEmail() {
        return getEmail("okta.com")
    }

    private Email createEmailAccount() {

        def jsonResponse = get(GUERILLA_MAIL_BASE + "?f=get_email_address").asString()

        try {
            return mapper.readValue(jsonResponse, Email.class)
        } catch (JsonMappingException e) {
            Assert.fail("Couldn't create an email address using GuerillaMail")
        }
    }
}