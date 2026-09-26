# Deep Dive Prompts

Use these prompts to move from a high-level walkthrough into a deeper technical
conversation.

## Prompts

- How would subscription state transitions be enforced in code?
- Which operations need idempotency keys and why?
- What business events should be auditable?
- How would payment-provider failures affect subscription state?
- Which metrics would detect billing correctness or operational drift?

## Strong Discussion Direction

The best answers should connect domain rules, persistence, tests, observability,
and provider boundaries without letting external payment details dominate the
first implementation slice.
