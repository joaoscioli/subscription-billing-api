# Interview Case Study

Use this case study to explain the project as a senior backend discussion.

## Scenario

A SaaS product needs to manage customers, plans, subscriptions, and simulated
billing events without turning the first version into a large distributed
system.

## Decision

Use a modular monolith with vertical slices. The subscription lifecycle is the
main slice because it connects product behavior, domain rules, persistence, API
contracts, tests, and audit events.

## Evidence To Show

- `src/main/java/com/joaoscioli/billing/subscriptions/SubscriptionService.java`
- `src/test/java/com/joaoscioli/billing/subscriptions/SubscriptionControllerTests.java`
- `docs/decisions/0006-model-subscription-renewal-as-a-domain-workflow.md`
- `docs/observability.md`

## Trade-Off

This is not trying to simulate a full payment platform. The project favors a
reviewable business workflow before adding real provider integration, invoicing
complexity, or distributed services.

## Strong Interview Close

"The important part is not the number of endpoints. It is that one business
workflow can be inspected from API contract to tests, database evolution, audit
history, and future roadmap."
