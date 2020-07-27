/*
 * Copyright 2018-Present Okta, Inc.
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

import com.okta.commons.lang.Strings;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public class OverlySimpleCsrfFilter extends OncePerRequestFilter {

    private static final String CSRF_KEY = "_csrf";

    private boolean shouldFilter(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);

        String method = httpRequest.getMethod().toUpperCase(Locale.ENGLISH);
        return "POST".equals(method) || "PUT".equals(method); // POST or PUT
    }

    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpSession session = WebUtils.toHttp(request).getSession(true);
        String expectedCsrf = (String) session.getAttribute(CSRF_KEY);

        // figure out the next CSRF token
        String nextCSRF = UUID.randomUUID().toString();
        request.setAttribute(CSRF_KEY, nextCSRF);

        if (shouldFilter(request)) {
            String actualCsrf = request.getParameter(CSRF_KEY);

            // if the csrf token does not match stop processing the filter
            if (Strings.isEmpty(expectedCsrf) || !expectedCsrf.equals(actualCsrf)) {
                request.getServletContext().log("CSRF token did not match");
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        chain.doFilter(request, response);

        // next key
        session.setAttribute(CSRF_KEY, nextCSRF);
    }
}