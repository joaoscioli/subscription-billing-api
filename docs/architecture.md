# Architecture

## Overview

Subscription Billing API is a modular Spring Boot backend for a simplified SaaS billing domain.

The system starts as a modular monolith. This is intentional: the project should demonstrate clear boundaries, testability, and operational readiness without introducing distributed-system complexity too early.

## Architectural Style

Initial style:

- REST API
- Modular monolith
- Layered Spring Boot application
- Domain-oriented package boundaries
- PostgreSQL as the system of record
- Flyway for schema migration
- JWT for stateless authentication

## Initial Modules

### Identity

Responsibilities:

- users;
- credentials;
- authentication;
- JWT issuing;
- role-based authorization.

### Organizations

Responsibilities:

- organization records;
- membership boundaries;
- tenant-aware access rules.

### Plans

Responsibilities:

- subscription plans;
- pricing metadata;
- plan activation and deactivation.

### Subscriptions

Responsibilities:

- customer subscription lifecycle;
- active, canceled, and expired states;
- business rules around duplicate active subscriptions.

### Billing Events

Responsibilities:

- simulated payment events;
- idempotency;
- event history;
- future integration point for messaging.

### Audit

Responsibilities:

- record important business actions;
- support traceability for security and billing operations.

## Request Flow

Typical authenticated request:

1. Client sends request with JWT.
2. Spring Security validates token and resolves the authenticated user.
3. Controller validates input and delegates to application service.
4. Service enforces business rules and authorization boundaries.
5. Repository persists or reads data through JPA.
6. Audit entry is recorded for relevant actions.
7. API returns DTO response.

## Data Storage

PostgreSQL is the primary database.

Flyway owns schema evolution. Every schema change should be represented as a migration.

Testcontainers should provide real PostgreSQL integration tests instead of relying only on mocks or in-memory behavior.

## Security

Initial approach:

- JWT access tokens;
- password hashing;
- role-based authorization;
- protected endpoints by default;
- public endpoints only for authentication and health checks.

Example roles:

- `ADMIN`
- `BILLING_MANAGER`
- `VIEWER`

## Testing Strategy

The project should include:

- unit tests for business rules;
- controller tests for API behavior;
- integration tests with PostgreSQL through Testcontainers;
- security tests for protected endpoints and roles;
- CI execution for every push and pull request.

## Observability

Initial observability should include:

- Spring Boot Actuator health endpoint;
- structured application logs;
- Micrometer metrics;
- documented local observability path.

Prometheus and Grafana can be added after the core API is stable.

## Architecture Constraints

- Keep the first version small enough to finish.
- Prefer explicit domain rules over generic abstractions.
- Do not introduce microservices before the modular monolith is clean.
- Do not add frontend complexity until the backend proves the main flow.
- Every public feature should have documentation and tests.
