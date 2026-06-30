# Quality Gates

This document defines the minimum quality checks expected before a change is
considered ready to merge.

The goal is not to add process for its own sake. The goal is to make quality
visible, repeatable, and easy to discuss during code review or technical
interviews.

## Current Merge Gate

Every pull request should pass the default CI workflow.

Current automated gate:

- compile the project with Java 21;
- run the Maven test suite;
- execute Flyway migrations during tests;
- execute PostgreSQL integration checks for migration fidelity;
- fail on broken application context;
- fail on API behavior regressions;
- fail on validation, persistence, or business-rule test failures.

Command used locally and in CI:

```bash
./mvnw -B --no-transfer-progress test
```

On Windows PowerShell:

```powershell
.\mvnw.cmd -B --no-transfer-progress test
```

PostgreSQL integration gate:

```bash
./mvnw -B --no-transfer-progress verify -Ppostgres-integration-tests
```

## Current Manual Review Gate

Before opening or merging a pull request, verify:

- the change has a clear purpose and small scope;
- the README or docs are updated when behavior changes;
- new business rules are covered by tests;
- migrations are versioned and backward-readable;
- API examples remain accurate;
- errors are handled through the global error response format;
- commits use readable conventional messages.

## Documentation Gate

Documentation should be updated when a change affects:

- API routes or payloads;
- setup or local execution;
- architecture boundaries;
- technical decisions;
- testing strategy;
- operational behavior;
- known limitations.

Relevant docs:

- [Architecture](architecture.md)
- [API Overview](api-overview.md)
- [Getting Started](getting-started.md)
- [Test Strategy](test-strategy.md)
- [Architecture Decision Records](decisions/)

## Testing Gate

For the current project stage, a change is acceptable when the default suite
protects the main behavior affected by the change.

Expected coverage by change type:

- controller behavior: MockMvc API tests;
- validation rules: valid and invalid request cases;
- persistence changes: Flyway migration execution in tests;
- organization-scoped resources: tests proving cross-organization isolation;
- subscription lifecycle rules: tests for status changes and duplicate active
  subscription prevention;
- future authentication changes: Spring Security tests.

## CI Gate

The CI workflow should stay fast enough to support small commits.

Current expectations:

- run on pushes to main and work branches;
- run on pull requests targeting main;
- use Java 21;
- cache Maven dependencies;
- use the Maven Wrapper;
- run the PostgreSQL integration profile in a separate job;
- cancel duplicate runs for the same branch;
- fail clearly when tests or compilation fail.

## Known Gaps

These gates are intentionally lightweight for the current stage. The next
maturity steps are:

- expand selected PostgreSQL Testcontainers tests;
- add security tests after JWT authentication is implemented;
- add coverage reporting when the domain stabilizes;
- add static analysis after the first public release is coherent;
- add build artifacts or Docker image validation when deployment is introduced.

## Interview Notes

In an interview, this quality-gate model can be explained as a staged approach:

1. Start with a fast default test suite so every commit is safe to push.
2. Document the manual review checklist so quality is not implicit.
3. Add heavier gates only when the project has behavior that justifies them.
4. Keep trade-offs visible instead of pretending the project is production
   complete before it is.

That is the senior engineering signal: quality is deliberate, staged, and tied
to risk.
