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
package com.okta.authn.sdk.impl.resource;

import com.okta.authn.sdk.resource.Factor;
import com.okta.authn.sdk.resource.Link;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.MapProperty;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultFactor extends AbstractResource implements Factor {

    private static final StringProperty ID_PROPERTY = new StringProperty("id");

    private static final StringProperty TYPE_PROPERTY = new StringProperty("factorType");

    private static final StringProperty PROVIDER_PROPERTY = new StringProperty("provider");

    private static final StringProperty VENDOR_NAME_PROPERTY = new StringProperty("vendorName");

    private static final StringProperty STATUS_PROPERTY = new StringProperty("status");

    private static final MapProperty PROFILE_PROPERTY = new MapProperty("profile"); // TODO: make this an object?

    private static final MapProperty LINKS_PROPERTY = new MapProperty("_links");

    private static final MapProperty EMBEDDED_PROPERTY = new MapProperty("_embedded");

    public DefaultFactor(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(
            ID_PROPERTY,
            TYPE_PROPERTY,
            PROVIDER_PROPERTY,
            VENDOR_NAME_PROPERTY,
            STATUS_PROPERTY,
            PROFILE_PROPERTY,
            LINKS_PROPERTY,
            EMBEDDED_PROPERTY
        );
    }

    @Override
    public String getId() {
        return getString(ID_PROPERTY);
    }

    @Override
    public String getStatus() {
        return getString(STATUS_PROPERTY);
    }

    @Override
    public String getType() {
        return getString(TYPE_PROPERTY);
    }

    @Override
    public String getProvider() {
        return getString(PROVIDER_PROPERTY);
    }

    @Override
    public String getVendorName() {
        return getString(VENDOR_NAME_PROPERTY);
    }

    @Override
    public Map<String, Object> getProfile() {
        return getMap(PROFILE_PROPERTY);
    }

    @Override
    public Map<String, Link> getLinks() {

        Map<String, Link> result = new LinkedHashMap<>();
        getMap(LINKS_PROPERTY).forEach((k,v) -> result.put((String) k, getDataStore().instantiate(Link.class, (Map<String, Object>) v)));
        return result;
    }

    @Override
    public Map<String, Object> getEmbedded() {
        return getMap(EMBEDDED_PROPERTY);
    }
}