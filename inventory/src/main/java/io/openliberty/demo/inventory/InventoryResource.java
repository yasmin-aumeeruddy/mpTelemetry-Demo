/*******************************************************************************
 * Copyright (c) 2022, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/

package io.openliberty.demo.inventory;

import java.util.Properties;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;

import io.openliberty.demo.inventory.model.InventoryList;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/systems")

public class InventoryResource {

    @Inject InventoryManager manager;

    @Inject
    Tracer tracer;

    @GET
    @Path("/{hostname}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPropertiesForHost(@PathParam("hostname") String hostname) {
        Span getPropertiesSpan = tracer.spanBuilder("GettingProperties").startSpan();
        Properties props = manager.get(hostname);
        try(Scope scope = getPropertiesSpan.makeCurrent()){
                if (props == null) {
                    getPropertiesSpan.addEvent("Cannot get properties");
                    return Response.status(Response.Status.NOT_FOUND)
                                .entity("{ \"error\" : \"Unknown hostname or the system "
                                + "service may not be running on " + hostname + "\" }")
                                .build();
                }
                getPropertiesSpan.addEvent("Received properties");
                manager.add(hostname, props);
        }
        finally{
            getPropertiesSpan.end();
        }    
        return Response.ok(props).build();
        
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public InventoryList listContents() {
        return manager.list();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearContents() {
        int cleared = manager.clear();

        if (cleared == 0) {
            return Response.status(Response.Status.NOT_MODIFIED)
                    .build();
        }
        return Response.status(Response.Status.OK)
                .build();
    }
}