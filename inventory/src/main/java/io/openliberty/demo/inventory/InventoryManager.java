
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

import java.util.ArrayList;
import java.util.Properties;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;

import io.openliberty.demo.inventory.client.SystemClient;
import io.openliberty.demo.inventory.model.InventoryList;
import io.openliberty.demo.inventory.model.SystemData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Collections;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped

public class InventoryManager {

    @Inject
    @ConfigProperty(name = "system.http.port")
    int SYSTEM_PORT;

    @Inject
    Tracer tracer;

    private List<SystemData> systems = Collections.synchronizedList(new ArrayList<>());
    private SystemClient systemClient = new SystemClient();
    
    public Properties get(String hostname) {
        systemClient.init(hostname, SYSTEM_PORT);
        Properties properties = systemClient.getProperties();
        return properties;
    }

    public void add(String hostname, Properties systemProps) {
        Span addPropertiesSpan = tracer.spanBuilder("addProperties").startSpan();
        Properties props = new Properties();
        props.setProperty("os.name", systemProps.getProperty("os.name"));
        props.setProperty("user.name", systemProps.getProperty("user.name"));
        SystemData system = new SystemData(hostname, props);
        if (!systems.contains(system)) {
            addPropertiesSpan.addEvent("Adding host to inventory");
            systems.add(system);
        }
        addPropertiesSpan.end();
    }

    @WithSpan
    public InventoryList list() {
        return new InventoryList(systems);
    }
    
    int clear() {
        int propertiesClearedCount = systems.size();
        systems.clear();
        return propertiesClearedCount;
    }
}

