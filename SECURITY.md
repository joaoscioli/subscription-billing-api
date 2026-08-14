# Security Policy

This repository is a portfolio project, but security concerns should still be
handled with production-minded care.

## Supported Scope

Security feedback is welcome for:

- authentication and authorization behavior;
- API input validation;
- dependency or build vulnerabilities;
- accidental secret exposure;
- insecure defaults in local configuration.

## Reporting

Please do not open a public issue with exploit details or sensitive data.

Send a private report to `joaoscioli@outlook.com` with:

- a short description of the risk;
- affected files, endpoints, or dependencies;
- steps to reproduce when possible;
- suggested mitigation if known.

## Security Expectations

- Secrets must not be committed.
- Example credentials must stay local-only and clearly documented.
- Authentication, authorization, and database changes should include tests or
  review notes.
