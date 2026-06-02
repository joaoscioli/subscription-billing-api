# ADR 0003: Use Flyway for Schema Migrations

## Status

Accepted

## Context

The API stores business data for organizations, customers, plans, and subscriptions. These records are part of the core domain, so database structure is not an implementation detail that can be left implicit.

In a professional backend project, schema evolution must be visible, repeatable, reviewable, and safe to run in CI. Relying on automatic schema generation hides important decisions and makes production changes harder to reason about.

## Decision

Use Flyway as the owner of database schema evolution.

Every schema change should be represented by a versioned SQL migration under:

```text
src/main/resources/db/migration
```

Current migrations:

- `V1__init.sql`
- `V2__create_organizations.sql`
- `V3__create_customers.sql`
- `V4__create_plans.sql`
- `V5__create_subscriptions.sql`

JPA entities must match the schema created by migrations. Hibernate should not be treated as the primary schema management tool.

## Consequences

Positive:

- Schema changes become explicit in code review.
- CI can validate migrations during tests.
- The database model becomes part of the portfolio story.
- Production-like environments can evolve predictably.
- Rollout planning becomes easier as the schema grows.

Trade-offs:

- Developers must write SQL migrations manually.
- Entity changes require discipline to keep migrations and Java mappings aligned.
- Rollbacks are not automatic and must be handled deliberately when needed.

## Alternatives Considered

### Hibernate DDL Auto

Rejected for schema ownership because it is convenient for prototypes but weak for production-minded systems and portfolio review.

### Liquibase

Considered valid, but Flyway was chosen because SQL migrations are simple, explicit, and easy to read for this project size.

### Manual Database Setup

Rejected because setup would become harder to reproduce locally and in CI.
