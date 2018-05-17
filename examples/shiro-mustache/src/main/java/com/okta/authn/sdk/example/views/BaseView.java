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
package com.okta.authn.sdk.example.views;

import io.dropwizard.views.View;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;

public abstract class BaseView extends View {

    protected BaseView(String templateName) {
        super(templateName);
    }

    protected BaseView(String templateName, @Nullable Charset charset) {
        super(templateName, charset);
    }

    public String getCsrf() {
        Subject subject = SecurityUtils.getSubject();
        HttpServletRequest request = WebUtils.getHttpRequest(subject);
        return (String) request.getAttribute("_csrf");
    }
}