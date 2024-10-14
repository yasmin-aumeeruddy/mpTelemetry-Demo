Clone the repo 
# Automatic 
Navigate to the system directory and start the server

`cd system`
`mvn liberty:run`

Navigate to the system endpoint:

`localhost:9080/system/properties`

# Manual 

In a spearate terminal, navigate to the inventory directory and start the server

`cd inventory`

`mvn liberty:run`

Traces are collected with automatic instrumentation.

Navigate to the inventory endpoint: 

`localhost:9081/inventory/systems/localhost`

You should see the manually created spans in the exporter endpoint.

# Running the backend exporters
See the [docker-otel-lgtm](https://github.com/grafana/docker-otel-lgtm) repository and clone the repo:

```
git clone https://github.com/grafana/docker-otel-lgtm.git \
cd docker-otel-lgtm/docker
```
Build and run the image containing all services required for gathering traces, metrics and logs: 

```
podman build . -t grafana/otel-lgtm \
podman run -p 3000:3000 -p 4317:4317 -p 4318:4318 --rm -ti localhost/grafana/otel-lgtm
```

# Viewing the telemetry data

Traces: 
http://localhost:3000/explore?schemaVersion=1&panes=%7B%22puw%22%3A%7B%22datasource%22%3A%22tempo%22%2C%22queries%22%3A%5B%7B%22refId%22%3A%22A%22%2C%22datasource%22%3A%7B%22type%22%3A%22tempo%22%2C%22uid%22%3A%22tempo%22%7D%2C%22queryType%22%3A%22traceqlSearch%22%2C%22limit%22%3A20%2C%22tableType%22%3A%22traces%22%2C%22filters%22%3A%5B%7B%22id%22%3A%22acc9142f%22%2C%22operator%22%3A%22%3D%22%2C%22scope%22%3A%22span%22%7D%5D%7D%5D%2C%22range%22%3A%7B%22from%22%3A%22now-1h%22%2C%22to%22%3A%22now%22%7D%7D%7D&orgId=1

Note: Click on the trace ID to visualise the spans and see them in a node graph

JVM Metrics: 
http://localhost:3000/d/b91844d7-121e-4d0a-93b8-a9c1a05703b3/jvm-overview-opentelemetry?orgId=1&from=now-5m&to=now

Note: These take longer to export than spans.

HTTP Metrics: 
http://localhost:3000/explore/metrics/trail?metric=http_server_request_duration_seconds_bucket&from=now-5m&to=now&var-ds=prometheus&var-filters=&refresh=&actionView=overview&layout=grid&var-groupby=$__all


Logs: 
http://localhost:3000/explore?schemaVersion=1&panes=%7B%22puw%22:%7B%22datasource%22:%22loki%22,%22queries%22:%5B%7B%22refId%22:%22A%22,%22expr%22:%22%22,%22queryType%22:%22range%22,%22datasource%22:%7B%22type%22:%22loki%22,%22uid%22:%22loki%22%7D%7D%5D,%22range%22:%7B%22from%22:%22now-1h%22,%22to%22:%22now%22%7D%7D%7D&orgId=1