# ADR 0001: Define Portfolio Project Scope

## Status

Accepted

## Context

The goal is to create a public portfolio project that demonstrates backend engineering maturity with Java and Spring Boot.

The project must be realistic enough to show professional skills, but small enough to complete and maintain.

Common portfolio risks:

- creating a CRUD that looks too simple;
- creating a system so large that it never reaches a polished state;
- adding many technologies without clear purpose;
- publishing code before documentation, tests, and setup are ready.

## Decision

Build a backend-first SaaS subscription billing API.

The first version will focus on:

- organizations;
- users;
- authentication;
- subscription plans;
- customer subscriptions;
- simulated billing events;
- audit log;
- tests;
- Docker;
- CI;
- API documentation.

The project will start as a modular monolith using Java 21, Spring Boot 3, PostgreSQL, Flyway, Spring Security, JUnit, Testcontainers, Docker Compose, GitHub Actions, and OpenAPI.

React and TypeScript may be added later as a thin product demo, but they are not required for the first backend release.

## Consequences

Positive:

- The project aligns with real backend job requirements.
- The scope supports security, persistence, testing, documentation, and architecture.
- The system can be completed incrementally.
- The repository can demonstrate senior engineering habits without needing a large codebase.

Trade-offs:

- It will not demonstrate complex frontend engineering in v0.1.
- It will not use real payment providers initially.
- It avoids microservices at first, even though distributed architecture may be a future topic.

## Alternatives Considered

### Generic CRUD API

Rejected because it would be easier to finish but less differentiated as a portfolio project.

### Full SaaS With Frontend First

Rejected for v0.1 because it increases scope and could weaken the backend focus.

### Microservices From The Start

Rejected because it adds operational complexity before the domain boundaries are proven.

### Real Payment Gateway Integration

Deferred because it creates external dependency and compliance concerns that are unnecessary for the first portfolio release.
