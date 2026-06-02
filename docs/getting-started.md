# Getting Started

This guide explains how to run, test, and inspect Subscription Billing API locally.

## Requirements

- Java 21
- Docker and Docker Compose
- Git

Maven does not need to be installed globally because the repository includes the Maven Wrapper.

## Clone

```bash
git clone https://github.com/joaoscioli/subscription-billing-api.git
cd subscription-billing-api
```

## Environment

The repository includes:

- `.env.example`
- `docker-compose.yml`
- Spring Boot local configuration

For local development, copy `.env.example` if you want to customize environment values:

```bash
cp .env.example .env
```

On Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

## Start PostgreSQL

```bash
docker compose up -d postgres
```

Check running containers:

```bash
docker compose ps
```

Stop local services:

```bash
docker compose down
```

## Run Tests

Linux/macOS:

```bash
./mvnw test
```

Windows PowerShell:

```powershell
.\mvnw.cmd test
```

The default test profile uses H2 for fast local and CI feedback. Flyway migrations still run during tests.

## Run The Application

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

## Inspect The API

When the application is running:

- Health: `http://localhost:8080/actuator/health`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Current Flow To Try

1. Create an organization.
2. Create a customer inside the organization.
3. Create a plan inside the organization.
4. Create a subscription linking the customer to the plan.
5. Cancel the subscription.
6. Create a new subscription for the same customer.

This flow demonstrates the current billing foundation and validates the main domain rules implemented so far.

## Useful Project Links

- [Architecture](architecture.md)
- [API Overview](api-overview.md)
- [Architecture Decision Records](decisions/)
- [Roadmap](../ROADMAP.md)
