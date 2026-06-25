# Authorization Model

This document defines the initial authorization direction for the Subscription
Billing API.

## Roles

Initial roles:

- `OWNER`: manages organization settings, users, plans, customers, and subscriptions.
- `ADMIN`: manages operational billing resources inside the organization.
- `MEMBER`: reads organization billing data and supports customer workflows.

## Organization Scope

Every authenticated user belongs to one organization.

Business requests should be evaluated with two questions:

1. Is the user authenticated?
2. Is the requested resource inside the user's organization scope?

## Initial Permission Direction

| Capability | OWNER | ADMIN | MEMBER |
| --- | --- | --- | --- |
| View organization data | Yes | Yes | Yes |
| Manage users | Yes | No | No |
| Manage plans | Yes | Yes | No |
| Manage customers | Yes | Yes | Yes |
| Manage subscriptions | Yes | Yes | No |
| Cancel subscriptions | Yes | Yes | No |

## Token Claims Direction

JWT claims should stay minimal:

- user id;
- email;
- organization id or slug;
- role;
- issued-at;
- expiration.

Mutable business data should be loaded from the database instead of being
overloaded into tokens.

## Interview Talking Points

- Authorization is separate from authentication.
- SaaS APIs need organization scoping.
- Roles should map to business capabilities.
- Token claims should be useful but minimal.
- Database checks may still be needed for sensitive authorization decisions.
