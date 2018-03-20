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

import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.authn.sdk.resource.AuthenticationStatus;
import com.okta.authn.sdk.resource.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

public class OktaRealm extends AuthorizingRealm {

    public OktaRealm() {
        // matching credentials on server
        setCredentialsMatcher(new AllowAllCredentialsMatcher());
        setAuthenticationTokenClass(OktaSuccessLoginToken.class);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        AuthenticationResponse authenticationResponse = ((OktaSuccessLoginToken) token).getAuthenticationResponse();

        // auth already verified, just check the status
        if (authenticationResponse != null && authenticationResponse.getStatus() == AuthenticationStatus.SUCCESS) {

            // if we have a valid User (see below) return an AuthenticationInfo
            User result = authenticationResponse.getUser();
            if (result != null) {
                SimplePrincipalCollection principalCollection = new SimplePrincipalCollection(result.getLogin(), getName());
                principalCollection.add(result, getName());

                return new SimpleAuthenticationInfo(principalCollection, null);
            }
        }

        return null; // returning null means the user is NOT authenticated
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
}