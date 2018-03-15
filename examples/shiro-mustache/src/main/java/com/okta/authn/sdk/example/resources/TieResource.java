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
package com.okta.authn.sdk.example.resources;

import com.okta.authn.sdk.example.dao.TieCraftDao;
import com.okta.authn.sdk.example.models.TieCraft;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.Collection;

/**
 * Tie Craft Resource.
 */
@Path("/tie")
@Produces("application/json")
public class TieResource {

    private final TieCraftDao tieCraftDao;

    @Inject
    public TieResource(TieCraftDao tieCraftDao) {
        this.tieCraftDao = tieCraftDao;
    }

    /**
     * Returns a Collection of all TieCraft.
     * @return Returns a Collection of all TieCraft.
     */
    @GET
    @RequiresPermissions("tie:read")
    public Collection<TieCraft> listTieCraft() {
        return tieCraftDao.listTieCrafts();
    }

    @Path("/{serial}")
    @GET
    @RequiresPermissions("tie:read")
    public TieCraft getTieCraft(@PathParam("serial") String serial) {
    TieCraft tieCraft = tieCraftDao.getTieCraft(serial);
        if (tieCraft == null) {
            throw new NotFoundException();
        }
        return tieCraft;
    }
}
