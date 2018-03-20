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

import com.okta.authn.sdk.AuthenticationStateHandler;
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
 *   <li>Automatically attempt to find your API credentials values in a number of default/conventional locations and then use
 *       the discovered values. Without any other configuration, the following locations will be each be checked,
 *       in order:</li>
 * </ul>
 *
 * <ol>
 *     <li>The environment variable {@code OKTA_CLIENT_TOKEN}.  If either of
 *         these values are present, they override any previously discovered value.</li>
 *     <li>A yaml file that exists at the file path or URL specified by the {@code okta.client.file}
 *         system property.  If this file exists and any values are present, the values override any
 *         previously discovered value.  The {@code okta.client.file} system property String can be an
 *         absolute file path, or it can be a URL or a classpath value by using the {@code url:} or
 *         {@code classpath:} prefixes respectively. Default value is {code ~/.okta/okta.yaml}. </li>
 *     <li>The system properties {@code okta.client.token}.  If this value is present, it will override any
 *     previously discovered values.</li>
 * </ol>
 *
 * <p><b>SECURITY NOTICE:</b> While the {@code okta.client.token} system property may be used to represent your
 * API Key Secret as mentioned above, this is not recommended: process listings on a machine will expose process
 * arguments (like system properties) and expose the secret value to anyone that can read process listings.  As
 * always, secret values should never be exposed to anyone other than the person that owns the API Key.</p>
 *
 * <p>While an API Key ID may be configured anywhere (and be visible by anyone), it is recommended to use a private
 * read-only file or an environment variable to represent API Key secrets.  <b>Never</b> commit secrets to source code
 * or version control.</p>
 *
 * <h2>Explicit API Key Configuration</h2>
 *
 * <p>The above default API Key searching heuristics may not be suitable to your needs.  In that case, you will likely
 * need to explicitly configure your API Key.  For example:</p>
 *
 * <pre>
 * ClientCredentials clientCredentials = new TokenClientCredentials("apiToken");
 *
 * AuthenticationClient client = {@link AuthenticationClients AuthenticationClients}.builder().setOrgUrl("https://example.okta.com").build();
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
     * Sets the base URL of the Okta REST API to use.  If unspecified, this value defaults to
     * {@code https://api.okta.com/v1} - the most common use case for Okta's public SaaS cloud.
     *
     * <p>Customers using Okta's Enterprise HA cloud might need to configure this to be
     * {@code https://enterprise.okta.io/v1} for example.</p>
     *
     * @param baseUrl the base URL of the Okta REST API to use.
     * @return the AuthenticationClientBuilder instance for method chaining
     */
    AuthenticationClientBuilder setOrgUrl(String baseUrl);

    AuthenticationClientBuilder setStateHandler(AuthenticationStateHandler stateHandler);

    /**
     * Constructs a new {@link AuthenticationClient} instance based on the AuthenticationClientBuilder's current configuration state.
     *
     * @return a new {@link AuthenticationClient} instance based on the AuthenticationClientBuilder's current configuration state.
     */
    AuthenticationClient build();
}
