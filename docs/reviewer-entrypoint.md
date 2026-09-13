# Reviewer Entrypoint

Use this page when you have only a few minutes to evaluate the repository.

## First Three Files

- `README.md` for the project purpose and navigation.
- `docs/technical-scope.md` for what the project proves today.
- `docs/architecture.md` for the main backend design decisions.

## What To Look For

- Clear separation between subscription, billing, and payment concerns.
- REST contracts that can evolve without hiding business rules.
- Explicit notes about security, testing, observability, and release readiness.

## Strong Interview Signal

The repository is strongest when discussed as a backend architecture case study:
how to design a billing domain carefully before connecting external providers or
adding production automation.
