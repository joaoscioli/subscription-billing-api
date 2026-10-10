# Senior Demo Path

Use this path when an interviewer asks for a fast technical walkthrough.

## 1. Domain first

Start with subscription lifecycle, billing boundaries, renewal rules, and failure scenarios. This shows the project is not only CRUD.

## 2. Architecture choices

Explain the modular monolith approach, vertical slices, API contracts, persistence boundaries, and why each choice keeps the system simple enough for a portfolio project while still realistic.

## 3. Engineering evidence

Show tests, ADRs, CI notes, authentication docs, observability notes, and release checklists. The goal is to prove repeatable engineering habits, not just feature delivery.

## 4. Next implementation slice

The strongest next slice is a complete subscription renewal workflow with idempotency, domain events, error handling, and integration tests.
