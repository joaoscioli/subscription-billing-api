# ADR 0005: Use JWT for API Authentication

## Status

Accepted

## Context

The project is evolving from a public domain API into a SaaS-style backend.
Organizations, customers, plans, and subscriptions already exist, but endpoints
are intentionally open while the domain foundation is being built.

The next security step needs to show realistic backend engineering without
turning the portfolio project into a full identity platform.

The authentication design should:

- protect business endpoints;
- support organization-scoped access;
- keep local development and tests understandable;
- avoid external identity-provider setup for the first public version;
- leave room for OAuth2 or an authorization server later.

## Decision

Use JWT access tokens for the first authenticated version of the API.

The first security slice will introduce:

- application users stored in the database;
- password hashing with Spring Security;
- login endpoint that returns a signed JWT access token;
- JWT validation through Spring Security;
- authenticated access for business endpoints;
- organization role claims or lookups for authorization decisions;
- security tests for public, authenticated, and unauthorized requests.

The first implementation should keep token lifetime, claims, and roles simple.
Refresh tokens, external OAuth2 login, and a separate authorization server are
deferred.

## Initial Endpoint Direction

Planned public endpoints:

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /actuator/health`
- `GET /actuator/info`
- OpenAPI endpoints during local portfolio review

Planned protected endpoints:

- organization management;
- customer management;
- plan management;
- subscription management.

## Token Claims Direction

The initial token should include enough information to identify the authenticated
principal and support future authorization.

Candidate claims:

- subject: user id;
- email;
- organization id or organization slug;
- role;
- issued-at time;
- expiration time.

Claims should stay minimal. Domain data that can change often should be loaded
from the database instead of overloading the token.

## Consequences

Positive:

- demonstrates practical API security;
- keeps the project self-contained for recruiters and reviewers;
- aligns with common Spring Boot backend job requirements;
- supports stateless API requests;
- allows focused security tests.

Trade-offs:

- issuing JWTs directly is simpler than a production identity platform;
- token revocation is not solved in the first slice;
- refresh-token rotation is deferred;
- role and membership changes may require short token lifetimes or database
  checks later.

## Alternatives Considered

### Keep Endpoints Public

Rejected for the next stage because the project is explicitly a SaaS-style
backend and must demonstrate authentication and authorization.

### OAuth2 Provider First

Deferred because it would add setup complexity before the project has its own
basic user and authorization model.

### Session-Based Authentication

Rejected for the API-first version because stateless JWT authentication is a
better fit for a backend portfolio API and future frontend/demo clients.

### Full Authorization Server

Deferred because it would shift the project away from subscription billing and
into identity-platform implementation.
