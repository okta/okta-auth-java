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
package com.okta.authn.sdk.example.dao;

import com.okta.authn.sdk.example.models.TieCraft;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Simple in memory DAO that initializes the collection with content found from http://starwars.wikia.com/wiki/TIE_line.
 */
public class DefaultTieCraftDao implements TieCraftDao {

    final private Map<String, TieCraft> tieCraftMap = Collections.synchronizedSortedMap(new TreeMap<String, TieCraft>());

    public DefaultTieCraftDao() {
        addTieCraft(new TieCraft("tie-11", "Starfighter", "Starfighter"));
        addTieCraft(new TieCraft("tie-22", "Shuttle", "Boarding Shuttle"));
        addTieCraft(new TieCraft("tie-33", "Starfighter", "Interceptor"));
        addTieCraft(new TieCraft("tie-44", "Starfighter", "Defender"));
        addTieCraft(new TieCraft("tie-55", "Light bomber", "Bomber"));
    }

    public TieCraft addTieCraft(TieCraft tieCraft) {
        tieCraftMap.put(tieCraft.getSerial(), tieCraft);
        return tieCraft;
    }

    @Override
    public Collection<TieCraft> listTieCrafts() {
        return Collections.unmodifiableCollection(tieCraftMap.values());
    }

    @Override
    public TieCraft getTieCraft(String serial) {
        return tieCraftMap.get(serial);
    }
}
