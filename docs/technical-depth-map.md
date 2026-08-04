# Technical Depth Map

This map connects the repository to the skills a technical reviewer may look for.

## Domain Modeling

- Subscription lifecycle rules are modeled explicitly.
- Audit events make business transitions traceable.
- Organization, customer, plan, and subscription boundaries are kept visible.

## Backend Architecture

- Modular monolith keeps the system compact and reviewable.
- Vertical slices make API, persistence, and tests easier to follow together.
- ADRs explain design choices instead of leaving them implicit.

## Testing And Reliability

- API tests protect request/response behavior.
- Domain tests protect lifecycle invariants.
- Migrations keep schema evolution repeatable.

## Operations

- Actuator, CI, quality gates, and release notes show production awareness.
- Observability is documented before the system becomes large.

## Interview Use

Start with the subscription lifecycle, then explain why the project favors clear
business workflows over generic CRUD endpoints.
