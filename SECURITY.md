# Security Policy

This repository is a portfolio-grade backend project and should avoid exposing
real credentials, secrets, tokens, or production data.

## Supported Scope

Security review focuses on:

- authentication and authorization behavior;
- API input validation;
- sensitive configuration handling;
- dependency risk;
- database migration safety;
- accidental secret exposure.

## Reporting A Security Concern

Do not open a public issue with secrets or exploitable details. Prefer a private
message or a minimal issue that states the affected area without exposing
sensitive data.

## Development Practices

- Keep secrets out of Git.
- Use environment variables for local credentials.
- Review API authorization before adding new endpoints.
- Update tests when security behavior changes.
