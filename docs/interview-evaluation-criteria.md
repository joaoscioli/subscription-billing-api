# Interview Evaluation Criteria

Use these criteria to evaluate the project during a technical interview.

## Baseline Signal

- Explains the subscription billing domain clearly.
- Navigates the REST API, architecture, and planning docs.
- Understands why payment-provider integration is out of scope for the first slice.

## Strong Signal

- Defends lifecycle state, validation, idempotency, and auditability.
- Connects tests and acceptance criteria to money-related risk.
- Explains how provider isolation keeps business rules testable.

## Senior Signal

- Discusses sequencing: domain behavior first, external integrations later.
- Identifies failure modes before implementation details.
- Balances delivery scope, correctness, observability, and future extensibility.
