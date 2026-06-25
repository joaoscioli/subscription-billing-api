# Reviewer Guide

This guide helps a technical reviewer evaluate the project quickly.

## Ten-Minute Review Path

1. Start with `README.md` to understand product scope and current API surface.
2. Read `docs/architecture.md` to understand the modular monolith approach.
3. Open `docs/api-examples.md` to see request and response examples.
4. Review `src/main/java/com/joaoscioli/billing/common/GlobalExceptionHandler.java`
   for consistent API error handling.
5. Review one vertical slice, such as `subscriptions`, from controller to
   repository and tests.
6. Read `docs/test-strategy.md` and `.github/workflows/ci.yml` to confirm the
   feedback loop.
7. Scan `docs/decisions/` for architecture decision records.

## Strong Signals

- The project is scoped as a realistic SaaS billing backend.
- Features are implemented as vertical slices with persistence, validation,
  API contract, and tests.
- Database changes are versioned with Flyway migrations.
- The API uses structured validation and error responses.
- Documentation explains decisions, trade-offs, and future roadmap.
- GitHub templates support review discipline.

## Current Gaps To Discuss Honestly

- Authentication is being built incrementally.
- Billing events are planned as the next business capability.
- Deployment is documented but not yet automated.
- Observability is documented and should evolve with real runtime metrics.

## Interview Framing

This repository should be presented as a compact backend system designed to
show engineering judgment: scope control, clean API design, test strategy,
database migration discipline, CI, documentation, and iterative delivery.
