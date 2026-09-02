# Reviewer FAQ

## What should I review first?

Start with the README review path, then read the architecture notes, API
overview, and production readiness matrix.

## What is the main technical signal?

The project models subscription and billing lifecycle behavior with explicit
boundaries instead of presenting a generic CRUD API.

## What is intentionally unfinished?

Real payment-provider integration, production secret management, and full
observability are documented as future hardening work.

## What should we discuss in an interview?

Discuss lifecycle rules, modular monolith trade-offs, API stability, testing,
and the path from portfolio project to production service.
