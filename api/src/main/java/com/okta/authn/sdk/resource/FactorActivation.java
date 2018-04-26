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
package com.okta.authn.sdk.resource;

import com.okta.sdk.resource.ExtensibleResource;

import java.util.Date;
import java.util.Map;

public interface FactorActivation extends ExtensibleResource {

    Map<String, Link> getLinks();

    // TOTP
    Integer getTimeStep();

    String getSharedSecret();

    String getEncoding();

    Integer getKeyLength();

    // Duo
    String getHost();

    String getSignature();

    String getFactorResult(); // Push

    Date getExpiresAt(); // push

    // UTF
    String getVersion();

    String getAppId();

    String getNonce();

    Integer getTimeoutSeconds();
}