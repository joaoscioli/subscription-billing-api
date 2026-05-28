# Subscription Billing API

A backend-first SaaS billing system built to demonstrate production-minded Java and Spring Boot engineering.

The project is intentionally scoped as a portfolio project: small enough to finish, realistic enough to show backend architecture, authentication, persistence, tests, CI, documentation, and operational concerns.

## Objective

Build a REST API for managing organizations, users, subscription plans, customer subscriptions, and simulated billing events.

This project should prove the ability to:

- design a clean and maintainable Spring Boot backend;
- model a business domain with real constraints;
- secure APIs with authentication and authorization;
- persist data with PostgreSQL and database migrations;
- validate behavior with unit and integration tests;
- provide a reproducible local environment with Docker;
- document architecture, API usage, and technical decisions;
- run automated checks through GitHub Actions.

## Target Audience

This repository is designed for engineering recruiters, hiring managers, and senior engineers reviewing backend portfolio work.

The goal is not to show a large system. The goal is to show clear engineering judgment in a compact system.

## Core Features v0.1

- Organization management
- User registration and authentication
- JWT-based API security
- Role-based access control
- Subscription plan management
- Customer subscription lifecycle
- Simulated payment events
- Audit log for important business actions
- OpenAPI documentation
- Automated tests
- Docker Compose local environment
- GitHub Actions CI pipeline

## Out of Scope for v0.1

- Real payment gateway integration
- Real email delivery
- Advanced frontend application
- Complex invoicing and taxation
- Multi-region deployment
- Kubernetes production setup

These topics may appear later as roadmap items, but they should not block the first public version.

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Web
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- JUnit 5
- Mockito
- Testcontainers
- Docker Compose
- GitHub Actions
- OpenAPI / Springdoc
- Spring Boot Actuator
- Micrometer

Optional later layer:

- TypeScript
- React
- Vite

## Project Documentation

- [Architecture](docs/architecture.md)
- [Roadmap](ROADMAP.md)
- [Architecture Decision Records](docs/decisions/)

## Initial Success Criteria

The first public release should be considered ready when:

- the project can be cloned and started with documented commands;
- automated tests pass locally and in CI;
- database schema is managed through migrations;
- authentication and authorization are working;
- at least one complete subscription lifecycle flow is implemented;
- README and architecture documentation explain the project clearly;
- OpenAPI docs are available when the application runs.

## Working Style

Development should happen through small issues and pull requests, even when working solo.

Recommended branch format:

- `feature/<short-description>`
- `fix/<short-description>`
- `docs/<short-description>`
- `chore/<short-description>`

Recommended commit style:

- `feat: add subscription creation endpoint`
- `fix: prevent duplicate active subscriptions`
- `test: add integration tests for billing flow`
- `docs: describe authentication flow`
- `ci: add build and test workflow`
