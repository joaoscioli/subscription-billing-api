# Tough Interview Questions

Use these questions to prepare direct, technical answers.

## Why is this not just CRUD?

Because the strongest slice models subscription lifecycle behavior: creation,
renewal, cancellation, validation, persistence, API contracts, and audit events.

## Why not microservices?

The current scope benefits from a modular monolith. It keeps deployment and
local review simple while still making domain boundaries visible.

## What is missing before production?

Real payment integration, stronger authorization coverage, invoice read models,
operational dashboards, and production-grade secret management.

## What would you defend in a code review?

The decision to make the subscription lifecycle explicit before adding payment
provider complexity.
