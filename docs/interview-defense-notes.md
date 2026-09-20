# Interview Defense Notes

Use these notes when a reviewer challenges the project scope or design choices.

## Likely Challenge

"Why is this not connected to a real payment provider yet?"

## Defense

Payment integration is intentionally later than subscription lifecycle modeling.
The project should first prove state transitions, validation, idempotency, and
testable business rules before adding vendor-specific complexity.

## Evidence To Show

- `docs/technical-scope.md`
- `docs/technical-risks.md`
- `docs/acceptance-criteria.md`

## Senior Signal

The decision protects the domain model from external-provider coupling and keeps
the first executable slice reviewable.
