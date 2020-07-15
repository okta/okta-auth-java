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

import com.okta.authn.sdk.resource.Link;
import com.okta.sdk.impl.ds.InternalDataStore;
import com.okta.sdk.impl.resource.AbstractResource;
import com.okta.sdk.impl.resource.ListProperty;
import com.okta.sdk.impl.resource.MapProperty;
import com.okta.sdk.impl.resource.Property;
import com.okta.sdk.impl.resource.StringProperty;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DefaultLink extends AbstractResource implements Link {

    private static final StringProperty HREF_PROPERTY = new StringProperty("href");

    private static final StringProperty NAME_PROPERTY = new StringProperty("name");

    private static final StringProperty TYPE_PROPERTY = new StringProperty("type");

    private static final MapProperty HINTS_PROPERTY = new MapProperty("hints");

    private static final ListProperty NESTED__ALLOW_PROPERTY = new ListProperty("allow");

    public DefaultLink(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return createPropertyDescriptorMap(
            HREF_PROPERTY,
            HINTS_PROPERTY,
            NAME_PROPERTY,
            TYPE_PROPERTY
        );
    }

    @Override
    public String getName() {
        return getString(NAME_PROPERTY);
    }

    @Override
    public String getType() {
        return getString(TYPE_PROPERTY);
    }

    @Override
    public List<String> getHintsAllow() {
        Map hints = getNonEmptyMap(HINTS_PROPERTY);
        return (List<String>) hints.getOrDefault(NESTED__ALLOW_PROPERTY.getName(), Collections.emptyList());
    }

    @Override
    public String getHref() {
        return getString(HREF_PROPERTY);
    }

    @Override
    public boolean hasNestedLinks() {
        return false;
    }

    @Override
    public List<Link> getNestedLinks() {
        return Collections.emptyList();
    }

    static Map<String, Link> getLinks(Map rawLinkMap, InternalDataStore dataStore) {

        Map<String, Link> result = new LinkedHashMap<>();
        if (!com.okta.commons.lang.Collections.isEmpty(rawLinkMap)) {
            rawLinkMap.forEach((k,v) -> {

                    Link link;
                    if(v instanceof Map) {
                        link = dataStore.instantiate(Link.class, (Map<String, Object>) v);

                    } else {
                        Map<String, Object> data = Collections.singletonMap("items", v);
                        link = dataStore.instantiate(NestedLink.class, data);
                    }
                    result.put((String) k, link);

            });
        }
        return Collections.unmodifiableMap(result);
    }
}