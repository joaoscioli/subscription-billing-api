# ADR 0002: Use Modular Monolith and Vertical Slices

## Status

Accepted

## Context

This project is a portfolio backend. It must demonstrate professional engineering judgment without becoming too large to finish or review.

A subscription billing domain can easily grow into a distributed system with separate services for identity, customers, plans, subscriptions, payments, invoices, events, and notifications. Starting with microservices too early would add deployment, observability, networking, consistency, and operational complexity before the domain boundaries are proven.

The project also needs a clear commit history. Each feature should be understandable by itself and should show the full path from database model to API behavior.

## Decision

Build the first version as a modular monolith organized by domain capability.

Current capability packages include:

- `organizations`
- `customers`
- `plans`
- `subscriptions`
- `common`
- `security`

Implement features as vertical slices. A slice should usually include:

- database migration;
- entity/model changes;
- repository access;
- service/business rule;
- controller endpoint;
- request/response DTOs;
- error handling;
- automated tests;
- README or architecture documentation when useful.

## Consequences

Positive:

- The system remains easy to run locally.
- The codebase is simpler to review in a portfolio context.
- Domain boundaries are visible without service-to-service complexity.
- Each commit can demonstrate end-to-end engineering work.
- Future extraction to services remains possible if boundaries mature.

Trade-offs:

- It does not demonstrate distributed deployment in the first version.
- Internal package boundaries require discipline because they are not enforced by network boundaries.
- Scaling concerns are mostly architectural discussion for now, not operational proof.

## Alternatives Considered

### Microservices From The Start

Rejected for the first version because it would shift attention away from domain modeling, tests, documentation, and API quality.

### Generic Layer-First Structure

Rejected as the main organization style because packages such as `controllers`, `services`, and `repositories` tend to hide business capabilities as the project grows.

### Single Package Prototype

Rejected because it would be faster initially but weaker as a senior portfolio artifact.
