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
6. Inspect the subscription event audit trail to see lifecycle traceability.
7. Read `docs/test-strategy.md` and `.github/workflows/ci.yml` to confirm the
   feedback loop.
8. Scan `docs/decisions/` for architecture decision records.

## Strong Signals

- The project is scoped as a realistic SaaS billing backend.
- Features are implemented as vertical slices with persistence, validation,
  API contract, and tests.
- Database changes are versioned with Flyway migrations.
- The API uses structured validation and error responses.
- Subscription lifecycle behavior is modeled with explicit workflows for
  creation, cancellation, and renewal instead of generic record updates.
- Subscription lifecycle events make operational traceability part of the
  domain model.
- Domain rules are verified through API-level tests, including conflict and
  invalid-state scenarios.
- Documentation explains decisions, trade-offs, and future roadmap.
- GitHub templates support review discipline.

## Current Gaps To Discuss Honestly

- Authentication is being built incrementally.
- Invoice and payment flows are planned as the next business capability.
- Deployment is documented but not yet automated.
- Observability is documented and should evolve with real runtime metrics.

## Next Practical Step

The next implementation step should add a minimal invoice read model connected
to subscription renewal events. That would turn the current lifecycle work into
a stronger billing-domain story without requiring a real payment provider.

## Interview Framing

This repository should be presented as a compact backend system designed to
show engineering judgment: scope control, clean API design, test strategy,
database migration discipline, CI, documentation, and iterative delivery.
