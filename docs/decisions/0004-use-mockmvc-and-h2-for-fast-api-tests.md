# ADR 0004: Use MockMvc and H2 for Fast API Tests

## Status

Accepted

## Context

The project needs automated tests that are easy to run locally and reliable in GitHub Actions. Since this is a portfolio project, a reviewer should be able to clone the repository and run the test suite without installing extra services first.

The current domain work is focused on HTTP API behavior, validation, persistence mapping, Flyway migration execution, and business rules for organizations, customers, plans, and subscriptions.

## Decision

Use Spring Boot tests with MockMvc as the main current test style.

Use the `test` profile with H2 for fast local and CI execution. Flyway migrations run during tests, so migration scripts are still validated as part of the automated suite.

Current test focus:

- API success scenarios;
- request validation failures;
- not-found behavior;
- duplicate-resource conflicts;
- subscription period calculation;
- subscription cancellation;
- organization scoping rules.

Testcontainers remains available in the project and should be introduced for PostgreSQL-specific integration coverage as the persistence model becomes more complex.

## Consequences

Positive:

- Tests are fast and easy to run.
- CI does not require Docker for the default test path.
- API behavior is validated through HTTP-like requests.
- Flyway migrations are exercised in the test lifecycle.
- The test suite supports small, frequent commits.

Trade-offs:

- H2 is not identical to PostgreSQL.
- Some SQL dialect differences may not be caught yet.
- Persistence behavior that depends on PostgreSQL-specific features needs future Testcontainers coverage.

## Alternatives Considered

### Unit Tests Only

Rejected because they would not prove request validation, controller behavior, migration execution, JSON contracts, and persistence integration.

### Testcontainers For Every Test

Deferred because it would improve database fidelity but make the default test loop heavier. It is still planned for selected integration tests.

### Manual API Testing Only

Rejected because portfolio-quality backend work needs repeatable automated verification.
