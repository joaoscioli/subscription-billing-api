# Reviewer Question Bank

Use these questions to prepare for a technical walkthrough.

## Questions To Expect

- How do you prevent duplicate subscription creation or duplicate charges?
- Why separate subscription lifecycle from payment-provider integration?
- Which state transitions should be impossible?
- Where would you add audit events and why?
- What would you test before connecting a real payment provider?

## Strong Answer Direction

Anchor answers in explicit state, idempotent commands, isolated provider
boundaries, and tests around business rules before vendor-specific behavior.
