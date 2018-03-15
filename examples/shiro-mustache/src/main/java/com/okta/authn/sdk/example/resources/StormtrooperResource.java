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

import com.okta.authn.sdk.example.models.Stormtrooper;
import com.okta.authn.sdk.example.dao.StormtrooperDao;
import com.okta.authn.sdk.example.views.StormtrooperView;
import com.okta.authn.sdk.example.views.StormtroopersView;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * Stormtrooper Resource.
 */
@Path("/troopers")
@Produces(MediaType.APPLICATION_JSON)
public class StormtrooperResource {

    private final StormtrooperDao trooperDao;

    @Inject
    public StormtrooperResource(StormtrooperDao trooperDao) {
        this.trooperDao = trooperDao;
    }

    /**
     * Returns a Collection of all Stormtroopers.
     * @return Returns a Collection of all Stormtroopers.
     */
    @GET
//    @RequiresPermissions("trooper:read")
    public Collection<Stormtrooper> listTroopers() {
        return trooperDao.listStormtroopers();
    }

    @GET
//    @RequiresPermissions("trooper:read")
    @Produces(MediaType.TEXT_HTML)
    public StormtroopersView listTroopersView() {
        return new StormtroopersView(listTroopers());
    }

    @GET
    @Path("/{id}")
//    @RequiresPermissions("trooper:read")
    public Stormtrooper getTrooper(@PathParam("id") String id) {

        Stormtrooper stormtrooper = trooperDao.getStormtrooper(id);
        if (stormtrooper == null) {
            throw new NotFoundException();
        }
        return stormtrooper;
    }

    @GET
    @Path("/{id}")
//    @RequiresPermissions("trooper:read")
    @Produces(MediaType.TEXT_HTML)
    public StormtrooperView getTrooperView(@PathParam("id") String id) {
        return new StormtrooperView(getTrooper(id));
    }

    @POST
//    @RequiresPermissions("trooper:create")
    public Stormtrooper createTrooper(Stormtrooper trooper) {

        return trooperDao.addStormtrooper(trooper);
    }

    @Path("/{id}")
    @POST
//    @RequiresPermissions("trooper:update")
    public Stormtrooper updateTrooper(@PathParam("id") String id, Stormtrooper updatedTrooper) {

        return trooperDao.updateStormtrooper(id, updatedTrooper);
    }

    @Path("/{id}")
    @DELETE
//    @RequiresPermissions("trooper:delete")
    public void deleteTrooper(@PathParam("id") String id) {
        trooperDao.deleteStormtrooper(id);
    }

}
