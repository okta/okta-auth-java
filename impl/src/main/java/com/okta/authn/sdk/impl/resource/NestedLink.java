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
import com.okta.sdk.impl.resource.AbstractCollectionResource;
import com.okta.sdk.impl.resource.Property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class NestedLink extends AbstractCollectionResource<Link> implements Link {

    public NestedLink(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return Collections.emptyMap();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public boolean hasNestedLinks() {
        return true;
    }

    @Override
    public List<Link> getNestedLinks() {
        return new ArrayList<>(getCurrentPage().getItems());
    }

    @Override
    protected Class<Link> getItemType() {
        return Link.class;
    }

    @Override
    public List<String> getHintsAllow() {
        return Collections.emptyList();
    }

    @Override
    public String getHref() {
        return null;
    }
}
