# Continuous Integration

This project uses GitHub Actions to run the automated test suite on pushes and
pull requests.

## Workflow

```text
.github/workflows/ci.yml
```

The workflow:

- checks out the repository;
- installs Java 21;
- caches Maven dependencies;
- runs `./mvnw -B --no-transfer-progress test`.

## Why This Matters

CI keeps the main backend project reviewable. A reviewer should be able to see
that changes are validated by automation instead of relying only on local
manual testing.

## Portfolio Signal

For interviews, this shows that the project is being treated like a real
backend service:

- tests are part of the delivery flow;
- the Maven Wrapper makes the build reproducible;
- GitHub Actions gives visible feedback on branches and pull requests.

## Next Improvements

- Add coverage reporting.
- Add Docker image build validation.
- Add a separate integration-test profile when Testcontainers coverage grows.
- Publish OpenAPI documentation as a CI artifact.
