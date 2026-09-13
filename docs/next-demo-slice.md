# Next Demo Slice

This is the smallest next implementation slice that would make the repository
stronger in a technical interview.

## Goal

Implement the subscription creation flow from request validation to persisted
subscription state.

## Scope

- Create a `POST /subscriptions` contract.
- Validate customer, plan, billing period, and start date.
- Persist subscription status as `ACTIVE` or `PENDING_PAYMENT`.
- Return a stable response DTO with business identifiers.
- Cover the flow with unit and integration tests.

## Interview Value

This slice proves that the documented architecture can become executable
backend behavior without jumping directly into external payment integration.
