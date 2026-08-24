# Architecture Review Checklist

Use this checklist to review the project's backend architecture. Support each item with code, tests, configuration, or documentation.

## Domain Boundary

- [ ] Subscription, plan, billing, cancellation, and renewal concepts are explicit.
- [ ] Domain services own business decisions; controllers coordinate requests.
- [ ] Payment providers remain behind explicit integration boundaries.

## API Boundary

- [ ] Endpoints expose use cases without leaking persistence details.
- [ ] Error responses are consistent and documented.
- [ ] Validation rejects invalid lifecycle transitions.

## Operational Readiness

- [ ] Logs, metrics, and audit events explain important subscription changes.
- [ ] Secrets and provider credentials remain outside source control.
- [ ] Known production gaps are explicit and prioritized.

## Architecture Defense

Be ready to explain why a modular monolith fits today, the evidence that it works, and the signal that would justify splitting billing, invoicing, or payment integration.
