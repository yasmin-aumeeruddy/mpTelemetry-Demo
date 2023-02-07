# Java Agent

Clone the repo 

Navigate to the system directory and run Maven with the `package` goal. This will copy the OpenTelemetry Java Agent in to your server config: 

`cd system`
`mvn package`

Start the server: 

`mvn liberty:run`

Without changes, the java agent will be collecting and exporting them to zipkin. You can change the service names and exporters etc... in the `jvm.options` files: 

https://github.com/yasmin-aumeeruddy/mpTelemetry-Demo/blob/main/system/src/main/liberty/config/jvm.options

Navigate to the system endpoint:

`localhost:9080/system/properties`

# Automatic Instrumentation

In a spearate terminal, navigate to the inventory directory and start the server

`cd inventory`

`mvn liberty:run`

Traces are collected with automatic instrumentation.

Navigate to the inventory endpoint: 

`localhost:9081/inventory/systems/localhost`

You should see the automatically created spans in the exporter endpoint.
# Manual Instrumentation

Manual instrumentation will be enabled if you uncomment the following: 

* [InventoryResource class imports](/blob/main/inventory/src/main/java/io/openliberty/demo/inventory/InventoryResource.java#L16-L18)
* [InventoryResource Tracer injection](/blob/main/inventory/src/main/java/io/openliberty/demo/inventory/InventoryResource.java#L38-L39)
* [InventoryResource Span creation](/blob/main/inventory/src/main/java/io/openliberty/demo/inventory/InventoryResource.java#L45-L47)
* [InventoryResource end Span](/blob/main/inventory/src/main/java/io/openliberty/demo/inventory/InventoryResource.java#L55)
---
* [InventoryManager class import](/blob/main/inventory/src/main/java/io/openliberty/demo/inventory/InventoryManager.java#L18)
* [InventoryManager WithSpan annotation](/blob/main/inventory/src/main/java/io/openliberty/demo/inventory/InventoryManager.java#L47)
* [InventoryManager WithSpan annotation](blob/main/inventory/src/main/java/io/openliberty/demo/inventory/InventoryManager.java#L58)

Navigate to the inventory endpoint: 

`localhost:9081/inventory/systems/localhost`

You should see the manually created spans in the exporter endpoint.