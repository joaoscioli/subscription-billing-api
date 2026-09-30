# Implementation Readiness

This file defines what must be clear before implementing the next backend slice.

## Ready When

- The subscription creation contract is stable enough for tests.
- Lifecycle states and invalid transitions are explicit.
- Validation rules are documented for customer, plan, billing period, and start date.
- Idempotency expectations are clear for command-style operations.
- Acceptance criteria describe success and failure paths.

## Not Ready If

- Payment-provider details are driving the domain model.
- Error responses are vague or inconsistent.
- Tests would need to guess business rules from implementation details.

## Review Focus

Implementation should begin with a small, testable Spring Boot slice that proves
the domain behavior before external payment integration.
