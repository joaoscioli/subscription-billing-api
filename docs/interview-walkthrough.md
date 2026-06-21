# Interview Walkthrough

This guide helps reviewers understand the repository in a short technical
interview.

## Suggested Reading Order

1. Read the README to understand the product scope.
2. Open the architecture document to see the backend boundaries.
3. Review the API examples to understand the external contract.
4. Inspect the test strategy and CI documentation.
5. Read the ADRs to understand important decisions.

## What This Project Demonstrates

- Java 21 and Spring Boot backend development.
- REST API design with validation and consistent errors.
- PostgreSQL persistence with Flyway migrations.
- Automated tests as part of the delivery flow.
- Operational thinking through Actuator, observability notes, and correlation
  ids.
- A roadmap that separates current scope from future expansion.

## Strong Talking Points

- Why the first version is intentionally backend-first.
- Why the project uses a modular monolith before distributed architecture.
- Why validation details and correlation ids matter for API consumers.
- Why a simulated billing flow is enough before real payment integration.
- How CI protects the main portfolio project.

## Honest Current Gaps

- JWT authentication is still being completed.
- Billing events are planned as the next realistic business layer.
- Full deployment is intentionally out of scope for the first version.
- Frontend is optional and should not distract from backend quality.

## Portfolio Signal

A clear walkthrough helps a senior reviewer see engineering judgment quickly.
It also shows that the repository was prepared for real technical discussion,
not only for a superficial scan.
