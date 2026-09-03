# Interview Red Flags

Use this file to avoid weak signals during a technical interview.

## Avoid Saying

- "It is just a CRUD API."
- "I would use microservices because they are modern."
- "Payment integration is simple; I would add Stripe later."
- "Tests can wait until the project is complete."

## Say Instead

- The project models subscription lifecycle behavior and revenue-sensitive rules.
- A modular monolith keeps boundaries clear while avoiding premature distribution.
- Payment providers should be isolated behind adapters and tested at the boundary.
- Tests should protect lifecycle behavior as soon as rules become explicit.
