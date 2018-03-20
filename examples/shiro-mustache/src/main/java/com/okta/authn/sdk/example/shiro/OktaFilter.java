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
package com.okta.authn.sdk.example.shiro;

import com.okta.authn.sdk.example.OktaStateHandler;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.authn.sdk.resource.AuthenticationStatus;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class OktaFilter extends AuthenticatingFilter {

    public OktaFilter() {
        this.appliedPaths.put(getLoginUrl(), null);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        return new OktaSuccessLoginToken(OktaStateHandler.getPreviousAuthResult());
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        if (isLoginRequest(request, response)) {
            return executeLogin(request, response);
        }

        if (pathsMatch(getLoginUrl(), request)) {
            return true;
        }

        saveRequestAndRedirectToLogin(request, response);
        return false;
    }

    @Override
    public boolean isLoginRequest(ServletRequest request, ServletResponse response) {

        AuthenticationResponse result = OktaStateHandler.getPreviousAuthResult();
        return result != null && result.getStatus().equals(AuthenticationStatus.SUCCESS);
    }
}