# Interview Questions

Use these questions to prepare a concise technical discussion about the project.

## What is the main engineering signal in this API?

The main signal is the subscription lifecycle implemented as a vertical slice:
domain rules, persistence, API contracts, tests, and audit events are connected
around the same business workflow.

## Why use a modular monolith here?

The project is small enough that a modular monolith keeps deployment and local
development simple while still preserving clear boundaries between
organizations, customers, plans, subscriptions, authentication, and operations.

## What would you improve next?

The next senior-level step is to connect renewals to invoice read models and
make the billing flow easier to inspect from the API.

## How do tests support the design?

Tests protect API behavior, validation rules, persistence assumptions, and the
subscription lifecycle. They make the project reviewable without requiring a
large production-like environment.
