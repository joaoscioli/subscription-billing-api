# Roadmap

This roadmap keeps the project small, reviewable, and useful as a portfolio artifact.

## v0.1 - Backend Foundation

Goal: establish the project foundation and first complete business flow.

- Create Spring Boot project with Java 21
- Add PostgreSQL and Flyway
- Add Docker Compose
- Model organizations and users
- Implement JWT authentication
- Implement role-based access control
- Model subscription plans
- Create customer subscriptions
- Add basic audit log
- Add OpenAPI documentation
- Add unit and integration tests
- Add GitHub Actions CI

Done when:

- tests pass locally and in CI;
- the application starts through Docker Compose;
- docs explain setup, architecture, and API usage;
- at least one subscription flow can be exercised end to end.

## v0.2 - Billing Events

Goal: make the system more realistic without integrating a real payment provider.

- Add simulated payment events
- Add idempotency key support for billing events
- Add subscription status transitions
- Add event history endpoint
- Add more integration tests with Testcontainers

Done when:

- duplicate billing events are safely handled;
- subscription status changes are covered by tests;
- API docs include examples.

## v0.3 - Observability and Operations

Goal: show production-minded backend practices.

- Add Spring Boot Actuator health endpoints
- Add Micrometer metrics
- Add structured logging conventions
- Add Prometheus/Grafana local setup if useful
- Add operational documentation

Done when:

- health and metrics endpoints are documented;
- the local observability stack can be started with Docker Compose;
- logs and metrics support basic troubleshooting.

## v0.4 - Frontend Demo

Goal: add a lightweight product demo without shifting the project away from backend.

- Create React + TypeScript frontend
- Add login flow
- Add dashboard for plans and subscriptions
- Add simple API client

Done when:

- backend remains independently usable;
- frontend demonstrates the main business flow;
- README explains both backend-only and full-demo modes.

## Future Ideas

- OAuth2 authorization server integration
- Refresh tokens
- Rate limiting
- RabbitMQ or Kafka for async billing events
- Contract tests
- Deployment preview
- Kubernetes manifests
- Terraform or infrastructure notes
