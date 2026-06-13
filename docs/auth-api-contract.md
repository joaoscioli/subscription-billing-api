# Authentication API Contract

This document defines the first public authentication API contract planned for
the Subscription Billing API.

## Register

```http
POST /api/auth/register
Content-Type: application/json
```

Request:

```json
{
  "organizationName": "Acme Inc",
  "organizationSlug": "acme",
  "email": "owner@acme.com",
  "password": "Str0ngPassword!"
}
```

Expected behavior:

- create the organization;
- create the first application user;
- assign the `OWNER` role;
- store the password with BCrypt;
- return the created user summary without password data.

Response direction:

```json
{
  "userId": "11111111-1111-1111-1111-111111111111",
  "organizationSlug": "acme",
  "email": "owner@acme.com",
  "role": "OWNER"
}
```

## Login

```http
POST /api/auth/login
Content-Type: application/json
```

Request:

```json
{
  "email": "owner@acme.com",
  "password": "Str0ngPassword!"
}
```

Response direction:

```json
{
  "accessToken": "jwt-token",
  "tokenType": "Bearer",
  "expiresInSeconds": 3600
}
```

## Validation Rules

- email must be valid and normalized to lowercase;
- password must satisfy minimum strength rules;
- organization slug must follow the existing slug format;
- duplicate organization slugs should return conflict;
- duplicate user emails should return conflict.

## Security Notes

- never return password hashes;
- keep JWT claims minimal;
- avoid putting mutable domain data into the token;
- protect business endpoints only after login and token validation tests exist.
