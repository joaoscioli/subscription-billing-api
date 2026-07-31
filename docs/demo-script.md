# Demo Script

Use this short script to present the project in a technical interview.

## 3-Minute Walkthrough

1. Start with the product scope: a SaaS billing backend for organizations,
   customers, plans, and subscriptions.
2. Show the subscription lifecycle endpoints: create, renew, cancel, and list
   lifecycle events.
3. Explain the strongest engineering signal: domain rules, Flyway migrations,
   API tests, and audit events are implemented in the same vertical slice.
4. Close with the next step: invoice read models connected to renewals.

## What To Emphasize

- The project is intentionally a modular monolith.
- Subscription behavior is modeled as business workflow, not generic CRUD.
- Auditability was added before real payment integration.

## Before The Interview

- Have one subscription lifecycle example ready.
- Be prepared to explain why audit events were added early.
- Know the next implementation step: invoice read models.

## Evidence To Open

- `src/main/java/com/joaoscioli/billing/subscriptions/SubscriptionService.java`
- `src/main/java/com/joaoscioli/billing/subscriptions/SubscriptionController.java`
- `src/test/java/com/joaoscioli/billing/subscriptions/SubscriptionControllerTests.java`
- `docs/api-overview.md`

## Avoid Saying

- "It is only a CRUD." Instead, explain the lifecycle rules and audit trail.
- "Payments are missing." Instead, explain why simulated billing comes before a
  real provider.
