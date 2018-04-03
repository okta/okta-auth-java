package com.okta.authn.sdk.example.views;

import io.dropwizard.views.View;
import org.apache.shiro.SecurityUtils;

import javax.annotation.Nullable;
import java.nio.charset.Charset;

public abstract class BaseView extends View {

    protected BaseView(String templateName) {
        super(templateName);
    }

    protected BaseView(String templateName, @Nullable Charset charset) {
        super(templateName, charset);
    }

    public String getCsrf(){
        return (String) SecurityUtils.getSubject().getSession().getAttribute("_csrf");
    }
}