# Changelog

This project follows small, reviewable increments. Dates use `YYYY-MM-DD`.

Review cadence: each portfolio update should explain the backend capability
added, the reviewer signal improved, and the next production-minded step.

## How To Discuss This History

Use this changelog to explain how the backend evolved from API foundation to
domain traceability. The strongest discussion is the subscription lifecycle:
rules first, persistence second, auditability third.

## Review Readiness

Ready for interview review when the README, API overview, reviewer guide, and
changelog tell the same story about the subscription lifecycle.

## Unreleased

- Planned: invoice read model connected to subscription renewals.
- Planned: stronger authentication and authorization coverage.

## 2026-07 Portfolio Hardening

- Added subscription lifecycle event recording.
- Exposed subscription audit events through the REST API.
- Documented lifecycle endpoints, reviewer path, and portfolio proof.
- Improved roadmap clarity around invoice and payment flows.
