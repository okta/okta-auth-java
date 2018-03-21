/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.okta.authn.sdk.example.events.impl;

import com.okta.authn.sdk.example.events.AuthenticationState;
import com.okta.authn.sdk.resource.AuthenticationStatus;
import org.apache.shiro.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnnotationStateHandlerResolver implements AuthenticationStateListenerResolver {

    private Class<? extends Annotation> annotationClass;

    public AnnotationStateHandlerResolver() {
        this.annotationClass = AuthenticationState.class;
    }

    /**
     * Returns a new collection of {@link AuthenticationStateListener} instances, each instance corresponding to an annotated
     * method discovered on the specified {@code instance} argument.
     *
     * @param instance the instance to inspect for annotated event handler methods.
     * @return a new collection of {@link AuthenticationStateListener} instances, each instance corresponding to an annotated
     *         method discovered on the specified {@code instance} argument.
     */
    public List<AuthenticationStateListener> getEventListeners(Object instance) {
        if (instance == null) {
            return Collections.emptyList();
        }

        List<Method> methods = ClassUtils.getAnnotatedMethods(instance.getClass(), getAnnotationClass());
        if (methods == null || methods.isEmpty()) {
            return Collections.emptyList();
        }

        List<AuthenticationStateListener> listeners = new ArrayList<>(methods.size());

        for (Method m : methods) {
            AuthenticationStatus status = m.getAnnotation(AuthenticationState.class).value();
            listeners.add(new SingleArgumentMethodAuthenticationStateListener(instance, m, status));
        }

        return listeners;
    }

    /**
     * Returns the type of annotation that indicates a method that should be represented as an {@link AuthenticationStateListener},
     * defaults to {@link AuthenticationState}.
     *
     * @return the type of annotation that indicates a method that should be represented as an {@link AuthenticationStateListener},
     *         defaults to {@link AuthenticationState}.
     */
    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    /**
     * Sets the type of annotation that indicates a method that should be represented as an {@link AuthenticationStateListener}.
     * The default value is {@link AuthenticationState}.
     *
     * @param annotationClass the type of annotation that indicates a method that should be represented as an
     *                        {@link AuthenticationStateListener}.  The default value is {@link AuthenticationState}.
     */
    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }
}
