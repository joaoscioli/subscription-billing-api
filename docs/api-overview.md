# API Overview

This document summarizes the current public API surface of Subscription Billing API.

The API is currently focused on the core billing foundation:

- organizations;
- customers;
- plans;
- subscriptions;
- subscription cancellation.

Authentication and authorization are planned but not active yet.

## Base Path

Current endpoints are served under:

```text
/api
```

## Error Response Shape

Domain and validation errors use a consistent JSON structure:

```json
{
  "timestamp": "2026-06-01T12:00:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Plan code already exists for organization: starter",
  "path": "/api/organizations/acme/plans"
}
```

## Organizations

Organizations are the current top-level business boundary.

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/api/organizations` | Create an organization |
| `GET` | `/api/organizations` | List organizations |
| `GET` | `/api/organizations/{slug}` | Find organization by slug |

Rules:

- slugs are unique;
- slugs must be lowercase URL-friendly identifiers.

## Customers

Customers belong to organizations.

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/api/organizations/{organizationSlug}/customers` | Create customer |
| `GET` | `/api/organizations/{organizationSlug}/customers` | List customers in organization |
| `GET` | `/api/organizations/{organizationSlug}/customers/{customerId}` | Find customer |

Rules:

- emails are normalized to lowercase;
- email uniqueness is enforced inside the organization;
- the same email can exist in different organizations.

## Plans

Plans define billable offers for an organization.

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/api/organizations/{organizationSlug}/plans` | Create plan |
| `GET` | `/api/organizations/{organizationSlug}/plans` | List plans in organization |
| `GET` | `/api/organizations/{organizationSlug}/plans/{code}` | Find plan by code |

Rules:

- plan code is unique inside the organization;
- `priceCents` must be greater than zero;
- `currency` is stored as uppercase ISO-style three-letter code;
- supported intervals are `MONTHLY` and `YEARLY`.

## Subscriptions

Subscriptions connect customers to plans.

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/api/organizations/{organizationSlug}/subscriptions` | Create subscription |
| `GET` | `/api/organizations/{organizationSlug}/subscriptions` | List subscriptions in organization |
| `GET` | `/api/organizations/{organizationSlug}/subscriptions/{subscriptionId}` | Find subscription |
| `POST` | `/api/organizations/{organizationSlug}/subscriptions/{subscriptionId}/cancel` | Cancel subscription |

Rules:

- new subscriptions start as `ACTIVE`;
- current period is calculated from the plan billing interval;
- one customer can have only one active subscription at a time;
- canceling a subscription sets status to `CANCELED`;
- a canceled subscription allows the customer to start a new active subscription.

## OpenAPI

When the application is running, generated OpenAPI documentation is available at:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

The generated docs are useful for exploration. This document exists as a curated portfolio-facing summary of current API behavior and domain rules.
