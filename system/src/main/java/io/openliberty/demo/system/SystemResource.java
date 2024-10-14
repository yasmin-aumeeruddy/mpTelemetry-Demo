
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

package io.openliberty.demo.system;

import java.util.Properties;

// CDI
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
// JAX-RS
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Random;
import io.opentelemetry.instrumentation.annotations.WithSpan;

import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
@Path("/properties")
public class SystemResource {

    private static final Logger julLogger = Logger.getLogger("jul-logger");

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProperties() throws InterruptedException {
        julLogger.log(Level.INFO, "Getting system properties");
        return Response.ok(System.getProperties())
            .build();
    }
}
