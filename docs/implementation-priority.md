# Implementation Priority

This file defines the next implementation order for turning the planning work
into a stronger executable backend project.

## P1: Subscription Lifecycle

- Implement subscription creation.
- Validate plan, customer, billing period, and start date.
- Persist explicit subscription status.
- Cover the lifecycle with unit and integration tests.

## P2: Billing Boundary

- Add billing cycle calculation.
- Separate billing rules from payment-provider concerns.
- Introduce idempotency for command-style operations.

## P3: Operational Readiness

- Add structured logs around lifecycle changes.
- Document metrics for created, renewed, cancelled, and failed subscriptions.
- Prepare a Docker-based local demo.

## Why This Order

The project should prove domain behavior before external integrations. That
keeps the interview discussion focused on architecture, tests, and business
rules instead of vendor-specific payment details.
