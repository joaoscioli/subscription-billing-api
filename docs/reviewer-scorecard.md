# Reviewer Scorecard

Use this scorecard to review the project quickly during portfolio screening.

## Strong Signals

- Business workflow modeled beyond simple CRUD.
- Spring Boot API, persistence, migrations, tests, and documentation evolve together.
- Subscription lifecycle events make operational history visible.
- Architecture decisions explain why the project is intentionally compact.

## Evidence

- `src/main/java/com/joaoscioli/billing/subscriptions/SubscriptionService.java`
- `src/test/java/com/joaoscioli/billing/subscriptions/SubscriptionControllerTests.java`
- `docs/decisions/0006-model-subscription-renewal-as-a-domain-workflow.md`
- `docs/test-strategy.md`

## Next Senior Step

Connect subscription renewals to invoice read models so the billing workflow
shows both command-side behavior and query-side visibility.
