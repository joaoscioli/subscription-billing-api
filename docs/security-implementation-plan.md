# Security Implementation Plan

This plan defines the next implementation steps for turning the authentication
foundation into protected API behavior.

## Step 1: Registration Endpoint

Implement:

- `POST /api/auth/register`;
- request validation;
- organization creation;
- owner user creation;
- BCrypt password hashing;
- conflict handling for duplicate organization slug or email.

Expected tests:

- successful owner registration;
- duplicate organization slug;
- duplicate email;
- weak password;
- password hash is never returned.

## Step 2: Login Endpoint

Implement:

- `POST /api/auth/login`;
- email and password authentication;
- stable error response for invalid credentials;
- token response shape.

Expected tests:

- successful login;
- wrong password;
- unknown email;
- disabled user.

## Step 3: JWT Infrastructure

Implement:

- token signing configuration;
- minimal claims;
- expiration;
- token validation filter;
- unauthorized response behavior.

Expected tests:

- valid token accepted;
- missing token rejected;
- malformed token rejected;
- expired token rejected.

## Step 4: Protect Business Endpoints

Implement:

- public auth and health endpoints;
- protected organization, customer, plan, and subscription endpoints;
- role checks;
- organization scope checks.

Expected tests:

- unauthenticated access rejected;
- authenticated access allowed;
- cross-organization access rejected;
- role-restricted action rejected.

## Interview Talking Points

- Security is being introduced incrementally.
- Authentication, token validation, and authorization are separate concerns.
- Organization scope is central to SaaS API design.
- Tests should cover both allowed and denied paths.
