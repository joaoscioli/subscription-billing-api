# Observability

This document describes the current operational visibility available in
Subscription Billing API and the next improvements planned for the project.

## Current Scope

The project uses Spring Boot Actuator for lightweight operational endpoints.

Exposed endpoints:

- `/actuator/health`
- `/actuator/info`
- `/actuator/metrics`

The current goal is to provide a simple, reviewable observability foundation
without adding a full monitoring stack too early.

## Health Endpoint

Use the health endpoint to verify whether the application is running and its
core health indicators are available:

```http
GET /actuator/health
```

Expected response shape:

```json
{
  "status": "UP"
}
```

Health probes are enabled so the application can evolve toward container and
deployment readiness checks.

## Info Endpoint

Use the info endpoint to inspect application metadata:

```http
GET /actuator/info
```

Current metadata includes:

- application name;
- project description;
- application version.

The version can be overridden through the `APP_VERSION` environment variable.

## Metrics Endpoint

Use the metrics endpoint to inspect available metric names:

```http
GET /actuator/metrics
```

Example follow-up query:

```http
GET /actuator/metrics/http.server.requests
```

This is useful for understanding the metrics that Micrometer exposes before
adding a Prometheus or Grafana setup.

## Automated Checks

The test suite verifies:

- `/actuator/health` returns `200 OK` and status `UP`;
- `/actuator/info` returns application metadata.

These checks are intentionally small. They prove that operational endpoints are
not accidentally broken while the API evolves.

## Current Trade-offs

The project does not yet include:

- Prometheus scraping;
- Grafana dashboards;
- structured logging conventions;
- alerting rules;
- distributed tracing.

Those are deferred because the current project stage is still focused on the
backend domain foundation. Adding a full observability stack too early would
make the repository heavier without adding proportional interview value yet.

## Next Improvements

Suggested next steps:

1. Add structured logging conventions for request and business events.
2. Add selected custom metrics for subscription lifecycle operations.
3. Add a local Prometheus/Grafana setup after the main billing flow is richer.
4. Document troubleshooting examples using health, logs, and metrics together.

## Interview Notes

The important point to explain is the staged approach:

- first expose health, info, and metrics;
- then test that operational endpoints stay available;
- then add richer metrics and dashboards when the domain has enough behavior to
  observe.

This shows production awareness without pretending the portfolio project is a
fully operated production system.
