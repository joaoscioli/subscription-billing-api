# Architecture

## Overview

Subscription Billing API is a Spring Boot backend for a simplified SaaS billing domain.

The project is intentionally built as a modular monolith. This keeps the first public version small enough to finish while still showing the engineering habits expected in production backend systems: clear boundaries, explicit business rules, database migrations, automated tests, CI, and documentation.

## Current Implementation Status

Implemented:

- Spring Boot 3 backend with Java 21
- REST API
- PostgreSQL-oriented persistence with JPA/Hibernate
- Flyway database migrations
- H2-backed automated test profile
- GitHub Actions CI
- Docker Compose for local PostgreSQL
- OpenAPI/Springdoc dependency
- Actuator dependency
- Organizations
- Customers scoped by organization
- Plans scoped by organization
- Subscriptions linking customers to plans
- Subscription cancellation
- Centralized API error responses
- MockMvc API tests for the current domain slices

Planned next:

- Authentication and authorization
- User accounts and organization membership
- Role-based access control
- ADRs for key implementation decisions
- Stronger OpenAPI examples
- Observability documentation
- Optional Testcontainers PostgreSQL integration tests

## Architectural Style

The application currently follows:

- RESTful API style
- Modular monolith deployment model
- Layered Spring Boot application structure
- Domain-oriented packages
- DTOs at the API boundary
- Service classes for application/business rules
- Spring Data JPA repositories for persistence
- Flyway migrations for schema ownership
- Automated API tests as the primary safety net

This shape is deliberately conservative. A portfolio backend should first prove that it can model a domain clearly, persist state safely, expose predictable APIs, and remain testable.

## Package Boundaries

Current main packages:

- `common`: shared API error response and global exception handling
- `organizations`: tenant-like organization records
- `customers`: customers that belong to an organization
- `plans`: billable plans owned by an organization
- `subscriptions`: subscription lifecycle between a customer and a plan
- `security`: initial Spring Security configuration

The package structure is organized by business capability instead of technical type alone. For example, customer controller, service, repository, DTOs, and exceptions live together under `customers`.

## Domain Model

### Organization

An organization is the current top-level boundary for business data.

Current rules:

- each organization has a unique slug;
- slugs are lowercase URL-friendly identifiers;
- customers, plans, and subscriptions are scoped by organization.

### Customer

A customer belongs to one organization.

Current rules:

- customer email is normalized to lowercase;
- email must be unique inside the same organization;
- the same email can exist in different organizations.

### Plan

A plan belongs to one organization and defines pricing metadata.

Current rules:

- plan code must be unique inside the same organization;
- plan code is a lowercase URL-friendly identifier;
- price is stored in minor units as `priceCents`;
- currency is stored as a three-letter uppercase code;
- billing interval is currently `MONTHLY` or `YEARLY`.

### Subscription

A subscription connects one customer to one plan inside the same organization.

Current rules:

- new subscriptions start as `ACTIVE`;
- the first billing period starts on `startsOn`;
- period end is calculated from the selected plan interval;
- a customer can have only one active subscription at a time;
- canceling a subscription changes status to `CANCELED`;
- a canceled subscription allows the customer to start a new active subscription.

## Request Flow

Current request flow:

1. Client sends an HTTP request.
2. Controller validates request body and path parameters.
3. Controller delegates to the service for the relevant domain capability.
4. Service loads required domain objects and enforces business rules.
5. Repository persists or reads data through JPA.
6. Domain-specific exceptions are converted into consistent API error responses.
7. Controller returns a response DTO.

Authentication is not active yet. Until the security slice is implemented, endpoints are intentionally permitted so the domain can evolve quickly and remain easy to test.

## Data Storage

PostgreSQL is the target production database.

Flyway owns schema evolution. Each schema change is represented as a versioned migration:

- `V1__init.sql`
- `V2__create_organizations.sql`
- `V3__create_customers.sql`
- `V4__create_plans.sql`
- `V5__create_subscriptions.sql`

Automated tests currently use an H2 profile for reliable local and CI execution without requiring Docker. Testcontainers remains a planned improvement for higher-fidelity PostgreSQL integration tests.

## Error Handling

The API uses a centralized `GlobalExceptionHandler`.

Current behavior:

- validation failures return `400 Bad Request`;
- duplicate organization slugs return `409 Conflict`;
- duplicate customer emails return `409 Conflict`;
- duplicate plan codes return `409 Conflict`;
- duplicate active subscriptions return `409 Conflict`;
- missing organizations, customers, plans, and subscriptions return `404 Not Found`.

Responses use a consistent shape through `ApiErrorResponse`.

## Testing Strategy

Current test strategy:

- `@SpringBootTest`
- `@AutoConfigureMockMvc`
- H2 test database
- Flyway migrations executed in tests
- HTTP-level API behavior checks
- success, validation, conflict, and not-found scenarios

The test suite currently validates the public behavior of organizations, customers, plans, subscriptions, and subscription cancellation.

Planned improvements:

- PostgreSQL integration tests with Testcontainers;
- security tests after JWT and role-based access control are added;
- focused unit tests for richer domain rules when the domain grows.

## Observability

The project includes Spring Boot Actuator and exposes health, info, and metrics
endpoints as the first operational visibility layer.

Current observability behavior:

- `/actuator/health` reports application health;
- `/actuator/info` reports application metadata;
- `/actuator/metrics` exposes Micrometer metric names and values;
- automated tests verify health and info endpoints.

Planned observability work:

- add structured logging conventions;
- add selected custom metrics for subscription lifecycle operations;
- optionally add Prometheus and Grafana local setup.

## Security Direction

Security is intentionally not the first domain slice. The project first establishes the billing model, then adds access control on top of a clearer domain.

Planned security direction:

- user accounts;
- organization membership;
- password hashing;
- JWT access tokens;
- role-based authorization;
- protected endpoints by default;
- security tests for authenticated and unauthorized access.

See [ADR 0005](decisions/0005-use-jwt-for-api-authentication.md) for the
authentication and JWT direction.

## Architecture Constraints

- Keep changes small and reviewable.
- Prefer vertical slices over large disconnected refactors.
- Keep business rules explicit in services and entities.
- Avoid microservices before the modular monolith is complete and useful.
- Do not add frontend complexity until the backend foundation is strong.
- Every public feature should have tests and documentation.

## Current Architecture Summary

The current system is a tested, CI-backed backend foundation for a SaaS subscription billing API. It is not finished, but it is already structured to show how a backend system grows: one domain capability at a time, with database migrations, HTTP APIs, business rules, documentation, and automated verification moving together.
