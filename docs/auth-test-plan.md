# Authentication Test Plan

This test plan defines the first authentication scenarios for the Subscription
Billing API.

## Registration Tests

- creates an organization and owner user;
- stores the password as a BCrypt hash;
- rejects duplicate organization slugs;
- rejects duplicate user emails;
- rejects weak passwords;
- rejects invalid organization slugs;
- never returns password hashes in responses.

## Login Tests

- returns a JWT for valid credentials;
- rejects unknown email;
- rejects wrong password;
- rejects disabled users;
- returns a stable error shape;
- does not reveal whether email or password was incorrect.

## Token Tests

- accepts a valid bearer token;
- rejects missing token on protected endpoints;
- rejects malformed token;
- rejects expired token;
- maps token subject to an application user;
- keeps token claims minimal.

## Authorization Tests

- owner can access organization resources;
- member cannot perform owner-only actions;
- user cannot access another organization scope;
- public endpoints remain available.

## Interview Talking Points

- Authentication tests should cover happy path, invalid credentials, and authorization boundaries.
- Error messages should avoid leaking credential details.
- Token tests should include missing, malformed, expired, and valid tokens.
- Organization scope matters in SaaS-style APIs.
