package com.okta.authn.sdk.example;

import com.okta.sdk.impl.http.MediaType;
import com.okta.sdk.lang.Strings;
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
        return ("POST".equals(method) || "PUT".equals(method)) // POST or PUT
            && httpRequest.getContentType().contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE); // form
    }

    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpSession session = WebUtils.toHttp(request).getSession(true);

        String expectedCsrf = (String) session.getAttribute(CSRF_KEY);
        // next key
        session.setAttribute(CSRF_KEY, UUID.randomUUID().toString());

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
    }
}