package com.okta.authn.sdk.doc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies a method that calls a remote API and adds an external documentation link.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface ApiReference {

    /**
     * API path represented by this method.
     */
    String path();

    /**
     * External documentation href.
     */
    String href();

}
