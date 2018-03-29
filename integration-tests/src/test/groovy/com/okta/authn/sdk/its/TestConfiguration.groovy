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

import com.okta.sdk.lang.Strings
import org.testng.TestException
import org.yaml.snakeyaml.Yaml

class TestConfiguration {

    final static File CONFIG_FILE = new File(System.properties.getProperty("user.home"), ".okta/okta.yaml")
    final static TestConfiguration CONFIG = new TestConfiguration()

    final Map localConfig

    private TestConfiguration() {

        if (CONFIG_FILE.exists()) {
            localConfig = new Yaml().load(CONFIG_FILE.text)
        } else {
            localConfig = Collections.emptyMap()
        }
    }

    String getMfaEnrollRequiredGroupId() {
        return getConfigValue("okta.authn.its.mfaEnrollGroupId")
    }

    String getPasswordWarningGroupId() {
        return getConfigValue("okta.authn.its.passwordWarningGroupId")
    }

    String getTwilioSid() {
        return getConfigValue("okta.authn.its.twilio.sid")
    }

    String getTwilioApiKey() {
        return getConfigValue("okta.authn.its.twilio.apiKey")
    }

    String getTelesignId() {
        return getConfigValue("okta.authn.its.telesign.customerId")
    }

    String getTelesignApiKey() {
        return getConfigValue("okta.authn.its.telesign.apiKey")
    }

    private String getConfigValue(String key) {
        String envVarKey = toEnvVar(key)
        String value = System.getProperty(key)
        if (Strings.isEmpty(value)) {
            value = System.getenv(envVarKey)
        }
        if (Strings.isEmpty(value)) {
            def position = localConfig
            for (def item : key.tokenize('.')) {
                position = position.get(item)
                if (!position) {
                    break
                }
            }
            value = position
        }

        if (value == null) {
            throw new TestException("Configuraion key: '${key}' is missing. Set the value as a System Property, Environment variable [${envVarKey}], or add the value to ${CONFIG_FILE.absolutePath}")
        }

        return value
    }

    private static String toEnvVar(String key) {
        return key.toUpperCase(Locale.ENGLISH).replaceAll("\\.", "_")
    }
}
