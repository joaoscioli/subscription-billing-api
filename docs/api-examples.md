# API Examples

This document gives reviewers quick examples of the intended API usage.

## Create Organization

```bash
curl -X POST http://localhost:8080/api/organizations \
  -H "Content-Type: application/json" \
  -H "X-Correlation-Id: demo-create-organization" \
  -d '{
    "name": "Acme Inc",
    "slug": "acme"
  }'
```

Expected result:

- HTTP `201 Created`;
- response body includes `id`, `name`, `slug`, `createdAt`, and `updatedAt`.

## Create Plan

```bash
curl -X POST http://localhost:8080/api/organizations/acme/plans \
  -H "Content-Type: application/json" \
  -H "X-Correlation-Id: demo-create-plan" \
  -d '{
    "name": "Starter",
    "code": "starter",
    "priceCents": 2900,
    "currency": "BRL",
    "billingInterval": "MONTHLY"
  }'
```

Expected result:

- HTTP `201 Created`;
- currency is normalized to uppercase;
- response includes organization and plan identifiers.

## Validation Error

```bash
curl -X POST http://localhost:8080/api/organizations/acme/plans \
  -H "Content-Type: application/json" \
  -H "X-Correlation-Id: demo-validation-error" \
  -d '{
    "name": "Free Trial",
    "code": "free-trial",
    "priceCents": 0,
    "currency": "BRL",
    "billingInterval": "MONTHLY"
  }'
```

Expected result:

- HTTP `400 Bad Request`;
- response includes `correlationId`;
- response includes field-level validation details.

## Portfolio Signal

API examples make the project easier to review quickly. They also show that the
backend is being designed as a consumer-facing contract, not only as internal
Java code.
