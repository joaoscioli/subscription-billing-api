# ADR 0006: Model Subscription Renewal As A Domain Workflow

## Status

Accepted

## Context

Subscriptions are not static records. A billing system needs to represent the
subscription lifecycle through explicit business operations, such as creation,
cancellation, renewal, plan changes, payment failure, and expiration.

The project already supported creating and canceling subscriptions. Renewal was
the next workflow because it proves that the API can evolve beyond CRUD and
protect business invariants over time.

## Decision

Subscription renewal is modeled as a domain workflow exposed by:

```text
POST /api/organizations/{organizationSlug}/subscriptions/{subscriptionId}/renew
```

The workflow:

- loads the subscription inside the organization boundary;
- rejects renewal unless the subscription is `ACTIVE`;
- moves `currentPeriodStart` to the previous `currentPeriodEnd`;
- calculates the next `currentPeriodEnd` from the plan billing interval;
- returns the updated subscription representation.

The entity owns the state transition guard, while the service orchestrates
loading, period calculation, transaction boundaries, and response mapping.

## Consequences

- The API now communicates real subscription lifecycle behavior.
- Tests can verify monthly, yearly, and invalid-state renewal scenarios.
- Future billing workflows can follow the same pattern: domain guard in the
  entity, orchestration in the service, public behavior verified at API level.
- More advanced scenarios, such as invoices, payment failures, trials, and plan
  changes, can build on this lifecycle model without rewriting the existing
  subscription contract.

## Alternatives Considered

- **Expose renewal as a generic update endpoint.** Rejected because generic
  updates hide business intent and make invariants easier to bypass.
- **Calculate renewal only in a scheduled job.** Deferred because the project
  needs a reviewable API workflow before adding background processing.
- **Store renewal events immediately.** Deferred until the project introduces an
  audit/event model.
