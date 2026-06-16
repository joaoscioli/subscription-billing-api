# Subscription Billing API

A backend-first SaaS billing system built to demonstrate production-minded Java and Spring Boot engineering.

The project is intentionally scoped as a portfolio project: small enough to finish, realistic enough to show backend architecture, authentication, persistence, tests, CI, documentation, and operational concerns.

## Objective

Build a REST API for managing organizations, users, subscription plans, customer subscriptions, and simulated billing events.

This project should prove the ability to:

- design a clean and maintainable Spring Boot backend;
- model a business domain with real constraints;
- secure APIs with authentication and authorization;
- persist data with PostgreSQL and database migrations;
- validate behavior with unit and integration tests;
- provide a reproducible local environment with Docker;
- document architecture, API usage, and technical decisions;
- run automated checks through GitHub Actions.

## Target Audience

This repository is designed for engineering recruiters, hiring managers, and senior engineers reviewing backend portfolio work.

The goal is not to show a large system. The goal is to show clear engineering judgment in a compact system.

## Core Features v0.1

- Organization management
- User registration and authentication
- JWT-based API security
- Role-based access control
- Subscription plan management
- Customer subscription lifecycle
- Simulated payment events
- Audit log for important business actions
- OpenAPI documentation
- Automated tests
- Docker Compose local environment
- GitHub Actions CI pipeline

## Out of Scope for v0.1

- Real payment gateway integration
- Real email delivery
- Advanced frontend application
- Complex invoicing and taxation
- Multi-region deployment
- Kubernetes production setup

These topics may appear later as roadmap items, but they should not block the first public version.

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Web
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- JUnit 5
- Mockito
- Testcontainers
- Docker Compose
- GitHub Actions
- OpenAPI / Springdoc
- Spring Boot Actuator
- Micrometer

Optional later layer:

- TypeScript
- React
- Vite

## Project Documentation

- [Architecture](docs/architecture.md)
- [Authentication](docs/authentication.md)
- [Authentication API Contract](docs/auth-api-contract.md)
- [Authentication Test Plan](docs/auth-test-plan.md)
- [Authorization Model](docs/authorization-model.md)
- [Test Strategy](docs/test-strategy.md)
- [Quality Gates](docs/quality-gates.md)
- [Observability](docs/observability.md)
- [Roadmap](ROADMAP.md)
- [Architecture Decision Records](docs/decisions/)

## Getting Started

### Requirements

- Java 21
- Docker and Docker Compose

The repository includes the Maven Wrapper, so a local Maven installation is not required.

### Start PostgreSQL

```bash
docker compose up -d postgres
```

### Run Tests

```bash
./mvnw test
```

On Windows PowerShell:

```powershell
.\mvnw.cmd test
```

### Run the Application

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Default local database settings are available in `.env.example` and `docker-compose.yml`.

### API Documentation

When the application is running, OpenAPI documentation is available at:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Health Check

Spring Boot Actuator health endpoint:

- `http://localhost:8080/actuator/health`

## Current API Surface

### Organizations

Create an organization:

```http
POST /api/organizations
Content-Type: application/json

{
  "name": "Acme Inc",
  "slug": "acme"
}
```

List organizations:

```http
GET /api/organizations
```

Find an organization by slug:

```http
GET /api/organizations/acme
```

Organization slugs must be lowercase URL-friendly identifiers, for example `acme`, `acme-inc`, or `billing-team`.

### Customers

Create a customer inside an organization:

```http
POST /api/organizations/acme/customers
Content-Type: application/json

{
  "name": "Ada Lovelace",
  "email": "ada@acme.com"
}
```

List customers from an organization:

```http
GET /api/organizations/acme/customers
```

Find a customer by id:

```http
GET /api/organizations/acme/customers/{customerId}
```

Customer emails are normalized to lowercase and must be unique inside the same organization.

### Plans

Create a billing plan inside an organization:

```http
POST /api/organizations/acme/plans
Content-Type: application/json

{
  "name": "Starter",
  "code": "starter",
  "priceCents": 2900,
  "currency": "BRL",
  "billingInterval": "MONTHLY"
}
```

List plans from an organization:

```http
GET /api/organizations/acme/plans
```

Find a plan by code:

```http
GET /api/organizations/acme/plans/starter
```

Plan codes must be lowercase URL-friendly identifiers and unique inside the same organization. Supported billing intervals are `MONTHLY` and `YEARLY`.

### Subscriptions

Create an active subscription for a customer:

```http
POST /api/organizations/acme/subscriptions
Content-Type: application/json

{
  "customerId": "11111111-1111-1111-1111-111111111111",
  "planCode": "starter",
  "startsOn": "2026-06-01"
}
```

List subscriptions from an organization:

```http
GET /api/organizations/acme/subscriptions
```

Find a subscription by id:

```http
GET /api/organizations/acme/subscriptions/{subscriptionId}
```

Cancel a subscription:

```http
POST /api/organizations/acme/subscriptions/{subscriptionId}/cancel
```

The first subscription flow creates an `ACTIVE` subscription and calculates the current billing period from the selected plan interval. A customer can have only one active subscription in this MVP. Canceling a subscription changes its status to `CANCELED` and allows the customer to start a new active subscription.

## Initial Success Criteria

The first public release should be considered ready when:

- the project can be cloned and started with documented commands;
- automated tests pass locally and in CI;
- database schema is managed through migrations;
- authentication and authorization are working;
- at least one complete subscription lifecycle flow is implemented;
- README and architecture documentation explain the project clearly;
- OpenAPI docs are available when the application runs.

## Working Style

Development should happen through small issues and pull requests, even when working solo.

Recommended branch format:

- `feature/<short-description>`
- `fix/<short-description>`
- `docs/<short-description>`
- `chore/<short-description>`

Recommended commit style:

- `feat: add subscription creation endpoint`
- `fix: prevent duplicate active subscriptions`
- `test: add integration tests for billing flow`
- `docs: describe authentication flow`
- `ci: add build and test workflow`
