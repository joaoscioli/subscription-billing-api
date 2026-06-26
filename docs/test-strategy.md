# Test Strategy

This document describes how the project validates behavior today and how the
test suite should evolve as the API becomes more production-like.

## Goals

The test suite should prove that the core subscription billing behavior works
through stable, repeatable checks that are easy to run locally and in CI.

Current goals:

- validate HTTP API contracts through realistic requests;
- exercise validation rules and error responses;
- verify persistence mappings and Flyway migrations;
- protect organization scoping rules;
- keep the feedback loop fast enough for small commits;
- make future PostgreSQL-specific coverage explicit instead of accidental.

## Current Test Layers

### Application Context

`SubscriptionBillingApiApplicationTests` verifies that the Spring application
context starts successfully with the test profile.

Why it matters: this catches broken configuration, missing beans, invalid
dependency wiring, and migration issues early.

### API Behavior Tests

Controller tests use Spring Boot, MockMvc, the `test` profile, and H2.

Covered areas:

- organization creation, listing, lookup, validation, and duplicate slug checks;
- customer creation, listing, lookup, email normalization, validation, and
  organization scoping;
- plan creation, listing, lookup, validation, and duplicate plan code checks;
- subscription creation, listing, lookup, period calculation, duplicate active
  subscription prevention, cancellation, and not-found behavior.

Why it matters: reviewers can see that behavior is verified from the HTTP
boundary instead of only through isolated method calls.

### Database Migration Checks

Flyway migrations run during the test lifecycle.

Why it matters: schema changes are not just documented; they are executed as
part of the automated feedback loop.

## Test Data Strategy

Tests should keep setup close to the behavior being verified.

Recommended conventions:

- create only the data required by the scenario;
- use readable organization slugs, plan codes, and customer emails;
- prefer helper methods when repeated setup would hide the test intention;
- keep assertions focused on API-visible behavior;
- clean database state between API scenarios.

## Local Commands

Run the default test suite:

```bash
./mvnw test
```

On Windows PowerShell:

```powershell
.\mvnw.cmd test
```

The default suite should stay fast and should not require Docker.

Run the optional PostgreSQL integration test suite:

```bash
./mvnw verify -Ppostgres-integration-tests
```

This profile runs Maven Failsafe tests named with the `IT` suffix against a
real PostgreSQL container through Testcontainers. It is useful before larger
persistence changes because it validates Flyway migrations against the target
database engine.

## CI Expectations

The CI pipeline should run the same default test suite used locally.

Minimum expected gate:

- checkout source code;
- set up Java 21;
- cache Maven dependencies when possible;
- run `./mvnw test`;
- fail the build on compilation errors, failed tests, or migration failures.

## Known Gaps

The current strategy is intentionally pragmatic, but not complete.

Current gaps:

- PostgreSQL-backed Testcontainers coverage is intentionally small and focused
  on migration fidelity;
- no dedicated service-level unit tests for complex business rules yet;
- no security/authentication flow tests yet;
- no contract tests for external consumers yet;
- no coverage reporting or quality threshold yet.

## Next Improvements

Suggested next steps:

1. Add GitHub Actions to run the default test suite on every push and pull
   request.
2. Expand Testcontainers coverage for PostgreSQL-specific persistence behavior.
3. Add security tests when JWT authentication and role-based authorization are
   implemented.
4. Add service-level tests for billing-event and subscription-status rules as
   the domain grows.
5. Add a lightweight quality gate for coverage or mutation testing only after
   the core behavior is stable.

## Mentoring Notes

A strong portfolio test strategy is not measured only by the number of tests.
It is measured by whether the tests protect important behavior, run reliably,
and explain the engineering trade-offs.

For this project, the senior-looking decision is to keep the default suite fast
while documenting exactly where deeper integration coverage belongs. That shows
judgment: not everything needs to be heavy, but the limitations should be known
and planned.
