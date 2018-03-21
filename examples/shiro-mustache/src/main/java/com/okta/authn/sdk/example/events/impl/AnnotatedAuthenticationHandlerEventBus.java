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

import com.okta.authn.sdk.AuthenticationStateHandlerAdapter;
import com.okta.authn.sdk.example.events.AuthenticationStateHandlerEventBus;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AnnotatedAuthenticationHandlerEventBus extends AuthenticationStateHandlerAdapter implements AuthenticationStateHandlerEventBus {

    private static final Logger log = LoggerFactory.getLogger(AnnotatedAuthenticationHandlerEventBus.class);

    private static final String EVENT_LISTENER_ERROR_MSG = "Event listener processing failed.  Listeners should " +
            "generally handle exceptions directly and not propagate to the event bus.";

    //this is stateless, we can retain a static final reference:
    private static final AuthenticationStateListenerComparator EVENT_LISTENER_COMPARATOR = new AuthenticationStateListenerComparator();

    private AnnotationStateHandlerResolver eventListenerResolver;

    //We want to preserve registration order to deliver events to objects in the order that they are registered
    //with the event bus.  This has the nice effect that any Shiro system-level components that are registered first
    //(likely to happen upon startup) have precedence over those registered by end-user components later.
    //
    //One might think that this could have been done by just using a ConcurrentSkipListMap (which is available only on
    //JDK 6 or later).  However, this approach requires the implementation of a Comparator to sort elements, and this
    //surfaces a problem: for any given random event listener, there isn't any guaranteed property to exist that can be
    //inspected to determine order of registration, since registration order is an artifact of this EventBus
    //implementation, not the listeners themselves.
    //
    //Therefore, we use a simple concurrent lock to wrap a LinkedHashMap - the LinkedHashMap retains insertion order
    //and the lock provides thread-safety in probably a much simpler mechanism than attempting to write a
    //EventBus-specific Comparator.  This technique is also likely to be faster than a ConcurrentSkipListMap, which
    //is about 3-5 times slower than a standard ConcurrentMap.
    private final Map<Object, Subscription> registry;
    private final Lock registryReadLock;
    private final Lock registryWriteLock;

    public AnnotatedAuthenticationHandlerEventBus() {
        this.registry = new LinkedHashMap<>(); //not thread safe, so we need locks:
        ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
        this.registryReadLock = rwl.readLock();
        this.registryWriteLock = rwl.writeLock();
        this.eventListenerResolver = new AnnotationStateHandlerResolver();
    }

    public AnnotationStateHandlerResolver getEventListenerResolver() {
        return eventListenerResolver;
    }

    public void setEventListenerResolver(AnnotationStateHandlerResolver eventListenerResolver) {
        this.eventListenerResolver = eventListenerResolver;
    }

    public void publish(AuthenticationResponse event) {
        if (event == null) {
            log.info("Received null event for publishing.  Ignoring and returning.");
            return;
        }

        registryReadLock.lock();
        try {
            //performing the entire iteration within the lock will be a slow operation if the registry has a lot of
            //contention.  However, it is expected that the very large majority of cases the registry will be
            //read-mostly with very little writes (registrations or removals) occurring during a typical application
            //lifetime.
            //
            //The alternative would be to copy the registry.values() collection to a new LinkedHashSet within the lock
            //only and the iteration on this new collection could be outside the lock.  This has the performance penalty
            //however of always creating a new collection every time an event is published,  which could be more
            //costly for the majority of applications, especially if the number of listeners is large.
            //
            //Finally, the read lock is re-entrant, so multiple publish calls will be
            //concurrent without penalty since publishing is a read-only operation on the registry.

            for (Subscription subscription : this.registry.values()) {
                subscription.onEvent(event);
            }
        } finally {
            registryReadLock.unlock();
        }
    }

    public void register(Object instance) {
        if (instance == null) {
            log.info("Received null instance for event listener registration.  Ignoring registration request.");
            return;
        }

        unregister(instance);

        List<AuthenticationStateListener> listeners = getEventListenerResolver().getEventListeners(instance);

        if (listeners == null || listeners.isEmpty()) {
            log.warn("Unable to resolve event listeners for subscriber instance [{}]. Ignoring registration request.",
                    instance);
            return;
        }

        Subscription subscription = new Subscription(listeners);

        this.registryWriteLock.lock();
        try {
            this.registry.put(instance, subscription);
        } finally {
            this.registryWriteLock.unlock();
        }
    }

    public void unregister(Object instance) {
        if (instance == null) {
            return;
        }
        this.registryWriteLock.lock();
        try {
            this.registry.remove(instance);
        } finally {
            this.registryWriteLock.unlock();
        }
    }

    private class Subscription {

        private final List<AuthenticationStateListener> listeners;

        public Subscription(List<AuthenticationStateListener> listeners) {
            List<AuthenticationStateListener> toSort = new ArrayList<>(listeners);
            Collections.sort(toSort, EVENT_LISTENER_COMPARATOR);
            this.listeners = toSort;
        }

        public void onEvent(AuthenticationResponse event) {

            Set<Object> delivered = new HashSet<>();

            for (AuthenticationStateListener listener : this.listeners) {
                if (listener.accepts(event) && !delivered.contains(listener)) {
                    try {
                        listener.onEvent(event);
                    } catch (Throwable t) {
                        log.warn(EVENT_LISTENER_ERROR_MSG, t);
                    }
                    delivered.add(listener);
                }
            }
        }
    }

    @Override
    public void handleUnknown(AuthenticationResponse unknownResponse) {
        publish(unknownResponse);
    }
}
