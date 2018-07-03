/*
 * Copyright 2014 Stormpath, Inc.
 * Modifications Copyright 2018 Okta, Inc.
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
package com.okta.authn.sdk.client;

import com.okta.sdk.client.Proxy;

/**
 * A <a href="http://en.wikipedia.org/wiki/Builder_pattern">Builder design pattern</a> used to
 * construct {@link AuthenticationClient} instances.
 *
 * <p>The {@code AuthenticationClientBuilder} is used to construct AuthenticationClient instances with Okta credentials,
 * Proxy and Cache configuration.  Understanding caching is extremely important when creating a AuthenticationClient instance, so
 * please ensure you read the <em>Caching</em> section below.</p>
 *
 * <h1>Usage</h1>
 *
 * <p>The simplest usage is to just call the {@link #build() build()} method, for example:</p>
 *
 * <pre>
 * AuthenticationClient client = {@link AuthenticationClients AuthenticationClients}.builder().{@link #build() build()};
 * </pre>
 *
 * <p>This will:</p>
 * <ul>
 *   <li>Automatically attempt to find your organization URL value in a number of default/conventional locations and then use
 *       the discovered values. Without any other configuration, the following locations will be each be checked,
 *       in order:</li>
 * </ul>
 *
 * <ol>
 *     <li>The environment variable {@code OKTA_CLIENT_ORGURL}. If this values is present, they override
 *     any previously discovered value.</li>
 *     <li>A yaml file that exists at the file path {@code ~/.okta/okta.yml} or root of the classpath {@code /okta.yml}.
 *         If this file exists and any values are present, the values override any
 *         previously discovered value.
*      </li>
 *     <li>The system properties {@code okta.client.orgUrl}.  If this value is present, it will override any
 *     previously discovered values.</li>
 * </ol>
 *
 * <h2>Explicit API Key Configuration</h2>
 *
 * <p>The above default configuration searching heuristics may not be suitable to your needs.  In that case, you will likely
 * need to explicitly configure the builder.  For example:</p>
 *
 * <pre>
 * AuthenticationClient client = {@link AuthenticationClients AuthenticationClients}.builder()
 *   .setOrgUrl("https://example.okta.com")
 *   .build();
 * </pre>
 *
 * @since 0.1.0
 */
public interface AuthenticationClientBuilder {

    String DEFAULT_CLIENT_ORG_URL_PROPERTY_NAME = "okta.client.orgUrl";
    String DEFAULT_CLIENT_CONNECTION_TIMEOUT_PROPERTY_NAME = "okta.client.connectionTimeout";
    String DEFAULT_CLIENT_AUTHENTICATION_SCHEME_PROPERTY_NAME = "okta.client.authenticationScheme";
    String DEFAULT_CLIENT_PROXY_PORT_PROPERTY_NAME = "okta.client.proxy.port";
    String DEFAULT_CLIENT_PROXY_HOST_PROPERTY_NAME = "okta.client.proxy.host";
    String DEFAULT_CLIENT_PROXY_USERNAME_PROPERTY_NAME = "okta.client.proxy.username";
    String DEFAULT_CLIENT_PROXY_PASSWORD_PROPERTY_NAME = "okta.client.proxy.password";
    String DEFAULT_CLIENT_REQUEST_TIMEOUT_PROPERTY_NAME = "okta.client.requestTimeout";
    String DEFAULT_CLIENT_RETRY_MAX_ATTEMPTS_PROPERTY_NAME = "okta.client.rateLimit.maxRetries";

    /**
     * Sets the HTTP proxy to be used when communicating with the Okta API server.  For example:
     *
     * <pre>
     * Proxy proxy = new Proxy("whatever.domain.com", 443);
     * AuthenticationClient client = {@link AuthenticationClients AuthenticationClients}.builder().setProxy(proxy).build();
     * </pre>
     *
     * @param proxy the {@code Proxy} you need to use.
     * @return the AuthenticationClientBuilder instance for method chaining.
     */
    AuthenticationClientBuilder setProxy(Proxy proxy);

    /**
     * Sets both the timeout until a connection is established and the socket timeout (i.e. a maximum period of inactivity
     * between two consecutive data packets).  A timeout value of zero is interpreted as an infinite timeout.
     *
     * @param timeout connection and socket timeout in milliseconds
     * @return the AuthenticationClientBuilder instance for method chaining
     */
    AuthenticationClientBuilder setConnectionTimeout(int timeout);

    /**
     * Sets the base URL of the Okta REST API to use.
     *
     * @param baseUrl the base URL of the Okta REST API to use.
     * @return the AuthenticationClientBuilder instance for method chaining
     */
    AuthenticationClientBuilder setOrgUrl(String baseUrl);

    /**
     * Sets the maximum number of milliseconds to wait when retrying before giving up.
     *
     * @param maxElapsed retry max elapsed duration in milliseconds
     * @return the ClientBuilder instance for method chaining
     */
    AuthenticationClientBuilder setRetryMaxElapsed(int maxElapsed);

    /**
     * Sets the maximum number of attempts to retrying before giving up.
     *
     * @param maxAttempts retry max attempts
     * @return the ClientBuilder instance for method chaining
     */
    AuthenticationClientBuilder setRetryMaxAttempts(int maxAttempts);

    /**
     * Constructs a new {@link AuthenticationClient} instance based on the AuthenticationClientBuilder's current configuration state.
     *
     * @return a new {@link AuthenticationClient} instance based on the AuthenticationClientBuilder's current configuration state.
     */
    AuthenticationClient build();
}
