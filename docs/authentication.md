# Authentication

This document describes the authentication direction for the Subscription
Billing API.

## Current Status

The project now has the first authentication foundation:

- `application_users` table managed by Flyway;
- application user entity;
- user roles: `OWNER`, `ADMIN`, and `MEMBER`;
- BCrypt password hashing;
- Spring Security `UserDetailsService` backed by the database.

Business endpoints are still temporarily open while the authentication slice is
introduced incrementally.

## Why This Step Matters

Authentication should be introduced in small slices because turning security on
for every endpoint at once can hide several different problems behind one large
change.

The current slice proves:

- user credentials can be stored safely;
- passwords are not stored as plain text;
- Spring Security can load application users from the database;
- database schema validation still works through Flyway and JPA;
- existing API behavior remains stable while auth is being built.

## Planned Flow

The initial authentication flow will be:

1. Register an application user.
2. Store the password with BCrypt.
3. Authenticate with email and password.
4. Return a signed JWT access token.
5. Use the token to access protected business endpoints.

## Public Endpoints Direction

Planned public endpoints:

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /actuator/health`
- `GET /actuator/info`
- OpenAPI endpoints during local portfolio review

## Protected Endpoints Direction

Planned protected endpoints:

- organization management;
- customer management;
- plan management;
- subscription management.

## Security Principles

- Store only password hashes, never plain passwords.
- Keep token claims minimal.
- Protect business endpoints after login and token validation are tested.
- Keep health checks available for operational visibility.
- Add tests for public, authenticated, and unauthorized access paths.

## Interview Talking Points

- Authentication is being introduced incrementally to reduce risk.
- BCrypt protects stored passwords.
- `UserDetailsService` integrates domain users with Spring Security.
- JWT will provide stateless API authentication.
- Authorization will use roles and organization scope.
