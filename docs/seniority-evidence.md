# Seniority Evidence

This file explains what senior-level signals the repository is meant to show.

## Evidence

- Separates subscription lifecycle, billing rules, and payment-provider concerns.
- Treats money-related workflows as stateful, auditable, and testable.
- Documents idempotency, risk, acceptance criteria, and implementation priority.
- Avoids adding external-provider complexity before the domain behavior is clear.

## Interview Signal

The project shows architectural sequencing: solve the business model and failure
modes first, then integrate external systems behind stable boundaries.
