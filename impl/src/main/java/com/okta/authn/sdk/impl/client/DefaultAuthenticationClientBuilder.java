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
package com.okta.authn.sdk.impl.client;

import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.client.AuthenticationClientBuilder;
import com.okta.sdk.client.AuthenticationScheme;
import com.okta.sdk.client.Proxy;
import com.okta.sdk.impl.config.ClientConfiguration;
import com.okta.sdk.impl.config.EnvironmentVariablesPropertiesSource;
import com.okta.sdk.impl.config.OptionalPropertiesSource;
import com.okta.sdk.impl.config.PropertiesSource;
import com.okta.sdk.impl.config.ResourcePropertiesSource;
import com.okta.sdk.impl.config.SystemPropertiesSource;
import com.okta.sdk.impl.config.YAMLPropertiesSource;
import com.okta.sdk.impl.io.ClasspathResource;
import com.okta.sdk.impl.io.DefaultResourceFactory;
import com.okta.sdk.impl.io.Resource;
import com.okta.sdk.impl.io.ResourceFactory;
import com.okta.sdk.impl.util.BaseUrlResolver;
import com.okta.sdk.impl.util.DefaultBaseUrlResolver;
import com.okta.sdk.lang.Assert;
import com.okta.sdk.lang.Strings;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>The default {@link AuthenticationClientBuilder} implementation. This looks for configuration files
 * in the following locations and order of precedence (last one wins).</p>
 * <ul>
 * <li>classpath:com/okta/sdk/config/okta.properties</li>
 * <li>classpath:okta.properties</li>
 * <li>classpath:okta.json</li>
 * <li>classpath:okta.yaml</li>
 * <li>~/.okta/okta.properties</li>
 * <li>~/.okta/okta.json</li>
 * <li>~/.okta/okta.yaml</li>
 * <li>~/okta.properties</li>
 * <li>~/okta.json</li>
 * <li>~/okta.yaml</li>
 * </ul>
 *
 * @since 0.1.0
 */
public class DefaultAuthenticationClientBuilder implements AuthenticationClientBuilder {

    private static final  String ENVVARS_TOKEN  = "envvars";
    private static final  String SYSPROPS_TOKEN = "sysprops";
    private static final String OKTA_YAML      = "okta.yaml";
    private static final String USER_HOME      = System.getProperty("user.home") + File.separatorChar;

    private static final String[] DEFAULT_OKTA_PROPERTIES_FILE_LOCATIONS = {
                                                 ClasspathResource.SCHEME_PREFIX + "com/okta/authn/sdk/config/" + OKTA_YAML,
                                                 ClasspathResource.SCHEME_PREFIX + OKTA_YAML,
                                                 USER_HOME + ".okta" + File.separatorChar + OKTA_YAML,
                                                 ENVVARS_TOKEN,
                                                 SYSPROPS_TOKEN
    };

    private ClientConfiguration clientConfig = new ClientConfiguration();

    public DefaultAuthenticationClientBuilder() {
        this(new DefaultResourceFactory());
    }

    DefaultAuthenticationClientBuilder(ResourceFactory resourceFactory) {
        Collection<PropertiesSource> sources = new ArrayList<>();

        for (String location : DEFAULT_OKTA_PROPERTIES_FILE_LOCATIONS) {

            if (ENVVARS_TOKEN.equalsIgnoreCase(location)) {
                sources.add(EnvironmentVariablesPropertiesSource.oktaFilteredPropertiesSource());
            }
            else if (SYSPROPS_TOKEN.equalsIgnoreCase(location)) {
                sources.add(SystemPropertiesSource.oktaFilteredPropertiesSource());
            }
            else {
                Resource resource = resourceFactory.createResource(location);

                PropertiesSource wrappedSource;
                if (Strings.endsWithIgnoreCase(location, ".yaml") ||
                        Strings.endsWithIgnoreCase(location, ".yml")) {
                    wrappedSource = new YAMLPropertiesSource(resource);
                } else {
                    wrappedSource = new ResourcePropertiesSource(resource);
                }

                PropertiesSource propertiesSource = new OptionalPropertiesSource(wrappedSource);
                sources.add(propertiesSource);
            }
        }

        Map<String, String> props = new LinkedHashMap<>();

        for (PropertiesSource source : sources) {
            Map<String, String> srcProps = source.getProperties();
            props.putAll(srcProps);
        }

        if (Strings.hasText(props.get(DEFAULT_CLIENT_ORG_URL_PROPERTY_NAME))) {
            String baseUrl = props.get(DEFAULT_CLIENT_ORG_URL_PROPERTY_NAME);
            // remove backslashes that can end up in file when it's written programmatically, e.g. in a test
            baseUrl = baseUrl.replace("\\:", ":");
            clientConfig.setBaseUrl(baseUrl);
        }

        if (Strings.hasText(props.get(DEFAULT_CLIENT_CONNECTION_TIMEOUT_PROPERTY_NAME))) {
            clientConfig.setConnectionTimeout(Integer.parseInt(props.get(DEFAULT_CLIENT_CONNECTION_TIMEOUT_PROPERTY_NAME)));
        }

        if (Strings.hasText(props.get(DEFAULT_CLIENT_AUTHENTICATION_SCHEME_PROPERTY_NAME))) {
            clientConfig.setAuthenticationScheme(Enum.valueOf(AuthenticationScheme.class, props.get(DEFAULT_CLIENT_AUTHENTICATION_SCHEME_PROPERTY_NAME)));
        }

        if (Strings.hasText(props.get(DEFAULT_CLIENT_PROXY_PORT_PROPERTY_NAME))) {
            clientConfig.setProxyPort(Integer.parseInt(props.get(DEFAULT_CLIENT_PROXY_PORT_PROPERTY_NAME)));
        }

        if (Strings.hasText(props.get(DEFAULT_CLIENT_PROXY_HOST_PROPERTY_NAME))) {
            clientConfig.setProxyHost(props.get(DEFAULT_CLIENT_PROXY_HOST_PROPERTY_NAME));
        }

        if (Strings.hasText(props.get(DEFAULT_CLIENT_PROXY_USERNAME_PROPERTY_NAME))) {
            clientConfig.setProxyUsername(props.get(DEFAULT_CLIENT_PROXY_USERNAME_PROPERTY_NAME));
        }

        if (Strings.hasText(props.get(DEFAULT_CLIENT_PROXY_PASSWORD_PROPERTY_NAME))) {
            clientConfig.setProxyPassword(props.get(DEFAULT_CLIENT_PROXY_PASSWORD_PROPERTY_NAME));
        }

        if (Strings.hasText(props.get(DEFAULT_CLIENT_RETRY_MAX_ELAPSED_PROPERTY_NAME))) {
            clientConfig.setRetryMaxElapsed(Integer.parseInt(props.get(DEFAULT_CLIENT_RETRY_MAX_ELAPSED_PROPERTY_NAME)));
        }

        if (Strings.hasText(props.get(DEFAULT_CLIENT_RETRY_MAX_ATTEMPTS_PROPERTY_NAME))) {
            clientConfig.setRetryMaxAttempts(Integer.parseInt(props.get(DEFAULT_CLIENT_RETRY_MAX_ATTEMPTS_PROPERTY_NAME)));
        }
    }

    @Override
    public AuthenticationClientBuilder setProxy(Proxy proxy) {
        if (proxy == null) {
            throw new IllegalArgumentException("proxy argument cannot be null.");
        }
        clientConfig.setProxyHost(proxy.getHost());
        clientConfig.setProxyPort(proxy.getPort());
        clientConfig.setProxyUsername(proxy.getUsername());
        clientConfig.setProxyPassword(proxy.getPassword());
        return this;
    }

    @Override
    public AuthenticationClientBuilder setConnectionTimeout(int timeout) {
        Assert.isTrue(timeout >= 0, "Timeout cannot be a negative number.");
        this.clientConfig.setConnectionTimeout(timeout);
        return this;
    }

    public AuthenticationClientBuilder setBaseUrlResolver(BaseUrlResolver baseUrlResolver) {
        Assert.notNull(baseUrlResolver, "baseUrlResolver must not be null");
        this.clientConfig.setBaseUrlResolver(baseUrlResolver);
        return this;
    }

    @Override
    public AuthenticationClientBuilder setRetryMaxElapsed(int maxElapsed) {
        this.clientConfig.setRetryMaxElapsed(maxElapsed);
        return this;
    }

    @Override
    public AuthenticationClientBuilder setRetryMaxAttempts(int maxAttempts) {
        this.clientConfig.setRetryMaxAttempts(maxAttempts);
        return this;
    }

    @Override
    public AuthenticationClient build() {

        if (this.clientConfig.getBaseUrlResolver() == null) {
            Assert.notNull(this.clientConfig.getBaseUrl(), "Okta org url must not be null.");
            this.clientConfig.setBaseUrlResolver(new DefaultBaseUrlResolver(this.clientConfig.getBaseUrl()));
        }

        this.clientConfig.setClientCredentialsResolver(new DisabledClientCredentialsResolver());
        return new DefaultAuthenticationClient(this.clientConfig);
    }

    @Override
    public AuthenticationClientBuilder setOrgUrl(String baseUrl) {
        Assert.notNull(baseUrl, "baseUrl argument cannot be null.");
        this.clientConfig.setBaseUrl(baseUrl);
        return this;
    }

    // Used for testing, package private
    ClientConfiguration getClientConfiguration() {
        return clientConfig;
    }
}