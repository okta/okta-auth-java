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
package com.okta.authn.sdk.example;

import com.codahale.metrics.health.HealthCheck;
import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.client.AuthenticationClients;
import com.okta.authn.sdk.example.dao.DefaultStormtrooperDao;
import com.okta.authn.sdk.example.dao.DefaultTieCraftDao;
import com.okta.authn.sdk.example.dao.StormtrooperDao;
import com.okta.authn.sdk.example.dao.TieCraftDao;
import com.okta.authn.sdk.example.events.AuthenticationState;
import com.okta.authn.sdk.example.events.impl.AnnotatedAuthenticationHandlerEventBus;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.jaxrs.ShiroFeature;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.apache.shiro.web.util.WebUtils;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;

import static com.okta.authn.sdk.resource.AuthenticationStatus.*;

public class ExampleApplication extends Application<ExampleConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ExampleApplication().run(args);
    }

    @Override
    public String getName() {
        return "shiro-example";
    }

    @Override
    public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
        // look up config yaml on the classpath
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
        bootstrap.addBundle(new AssetsBundle("/assets", "/static", null));
        bootstrap.addBundle(new ViewBundle<ExampleConfiguration>() {

            @Override
            public Map<String, Map<String, String>> getViewConfiguration(ExampleConfiguration config) {
                return config.getViews();
            }
        });
    }

    @Override
    public void run(final ExampleConfiguration configuration, final Environment environment) {
        // example health check
        environment.healthChecks().register("example", new HealthCheck() {
            @Override
            protected HealthCheck.Result check() {
                // Everything is in memory, so we are always healthy ;)
                return Result.healthy();
            }
        });

        configureShiro(environment);
        configureJersey(environment.jersey());
    }

    private void configureShiro(final Environment environment) {

        // One line to enable Shiro
        environment.jersey().register(ShiroFeature.class); // JAX-RS Feature

        // Dropwizard does not load servlet fragments, so we must configure the servlet filter
        environment.servlets().addServletListeners(new EnvironmentLoaderListener());
        environment.servlets().addFilter("ShiroFilter", ShiroFilter.class)
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

    private void configureJersey(JerseyEnvironment jersey) {

        // Load any resource in the resources package
        String baseResourcePackage = getClass().getPackage().getName() + ".resources";
        jersey.packages(baseResourcePackage);

        AnnotatedAuthenticationHandlerEventBus handler = new AnnotatedAuthenticationHandlerEventBus();
        handler.register(this);
        AuthenticationClient client = AuthenticationClients.builder()
                                                           .setStateHandler(handler)
                                                           .build();

        // use @Inject to bind the DAOs
        jersey.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(new DefaultStormtrooperDao()).to(StormtrooperDao.class);
                bind(new DefaultTieCraftDao()).to(TieCraftDao.class);
                bind(client).to(AuthenticationClient.class);
            }
        });
    }

    @AuthenticationState(SUCCESS)
    public void handleSuccess(AuthenticationResponse successResponse) {
        // the last request was a success, but if we do not have a session token
        // we need to force the flow to start over
        String relayState = successResponse.getRelayState();
        String dest = relayState != null ? relayState : "/troopers";
        redirect(dest, successResponse);
    }

    @AuthenticationState(PASSWORD_EXPIRED)
    public void handlePasswordExpired(AuthenticationResponse passwordExpired) {
        redirect("/login/change-password", passwordExpired);
    }

    @AuthenticationState(MFA_REQUIRED)
    public void handleMfaRequired(AuthenticationResponse mfaRequiredResponse) {
        redirect("/login/mfa", mfaRequiredResponse);
    }

    @AuthenticationState(MFA_CHALLENGE)
    public void handleMfaChallenge(AuthenticationResponse mfaChallengeResponse) { }

    @AuthenticationState(UNKNOWN)
    public void handleUnknown(AuthenticationResponse unknownResponse) {
        redirect("/login?error="+ unknownResponse.getStatus().name(), unknownResponse);
    }

    private void redirect(String location, AuthenticationResponse authenticationResponse) {

        try {
            Subject subject = SecurityUtils.getSubject();
            HttpServletResponse response = WebUtils.getHttpResponse(subject);
            OktaAuthenticationStateHandler.setAuthNResult(authenticationResponse);
            response.sendRedirect(location);
        } catch (IOException e) {
            throw new IllegalStateException("failed to redirect.", e);
        }
    }
}