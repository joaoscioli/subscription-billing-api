# Release Checklist

This checklist defines what should be true before a portfolio release is marked
as ready for review.

## Code

- Main business flow is implemented and tested.
- Validation errors return field-level details.
- API errors include a correlation id.
- Database migrations are committed and reproducible.
- Security rules are covered by tests where possible.

## Tests

- Unit and integration tests pass locally.
- GitHub Actions workflow is green.
- New behavior has at least one meaningful test.
- Failure paths are tested for important business rules.

## Documentation

- README explains the project purpose and setup.
- API examples match current endpoints.
- Architecture documentation reflects current package boundaries.
- ADRs explain important decisions.
- Roadmap clearly separates current scope from future ideas.

## Operations

- Docker Compose starts required local infrastructure.
- Actuator endpoints are documented.
- Logs and correlation ids support troubleshooting.
- Secrets are not committed.

## Portfolio Signal

A release checklist shows that the repository is being treated as a productized
backend artifact, not only as a code dump.
