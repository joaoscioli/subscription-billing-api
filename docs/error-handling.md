# Error Handling

This document defines how the API should return errors in a predictable,
client-friendly, and debuggable way.

## Goal

Every error response should help the API consumer understand:

- what failed;
- why it failed;
- whether the request can be fixed by the client;
- how to reference the failure in logs or support conversations.

## Standard Error Shape

```json
{
  "timestamp": "2026-06-17T10:15:30Z",
  "status": 400,
  "error": "validation_error",
  "message": "Request validation failed.",
  "path": "/api/v1/subscription-plans",
  "correlationId": "f2f8e1f0-7c2f-4a91-94fb-1f8c2f9fd111",
  "details": [
    {
      "field": "price",
      "message": "must be greater than zero"
    }
  ]
}
```

## Error Categories

### Validation Errors

Use HTTP `400 Bad Request` when the request body, query parameter, or path
parameter is syntactically valid but violates application validation rules.

Examples:

- missing required field;
- invalid enum value;
- amount below the minimum allowed value;
- malformed email address.

### Authentication Errors

Use HTTP `401 Unauthorized` when the request does not include valid
authentication credentials.

Examples:

- missing bearer token;
- expired token;
- invalid token signature.

### Authorization Errors

Use HTTP `403 Forbidden` when the user is authenticated but not allowed to
perform the requested action.

Examples:

- non-admin user trying to create a subscription plan;
- user trying to access another organization.

### Not Found Errors

Use HTTP `404 Not Found` when a requested resource does not exist or should not
be exposed to the current user.

Examples:

- unknown subscription plan id;
- inactive customer subscription;
- resource outside the current organization boundary.

### Conflict Errors

Use HTTP `409 Conflict` when the request is valid but conflicts with the current
state of the system.

Examples:

- duplicated organization slug;
- subscription already canceled;
- plan cannot be removed because active subscriptions depend on it.

### Internal Errors

Use HTTP `500 Internal Server Error` for unexpected failures. The response
should be generic and must not leak stack traces, SQL statements, credentials,
or infrastructure details.

## Correlation Id

Each response should include a correlation id. The same id should appear in
application logs so production issues can be investigated without exposing
internal implementation details to the API consumer.

## Implementation Notes

- Centralize exception mapping with `@ControllerAdvice`.
- Keep domain exceptions explicit and meaningful.
- Do not return raw exception messages to clients.
- Keep validation errors field-oriented.
- Log internal failures with enough context for debugging.

## Portfolio Signal

Consistent error handling shows that the API was designed for real consumers,
not only for happy-path demos. It also connects application design,
observability, security, and client experience.
