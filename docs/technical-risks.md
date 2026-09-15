# Technical Risks

This file captures the main risks a reviewer should expect in a subscription
billing backend and how the project plans to reduce them.

## Risks

- Incorrect subscription state transitions can create billing inconsistencies.
- Payment-provider coupling can make business rules hard to test.
- Missing idempotency can duplicate charges or subscription events.
- Weak auditability can make customer disputes hard to investigate.

## Mitigations

- Model subscription status changes explicitly.
- Keep payment-provider integration behind a boundary.
- Add idempotency keys to command-style operations.
- Record business events for important lifecycle changes.

## Interview Angle

The strongest discussion is how to protect money-related workflows with clear
state, repeatable commands, tests, and audit trails.
