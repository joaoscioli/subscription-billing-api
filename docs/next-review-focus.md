# Next Review Focus

This file defines the next focused improvement for a technical reviewer to watch.

## Current Focus

Connect subscription renewals to invoice read models.

## Why It Matters

The project already shows lifecycle commands and audit events. Invoice read
models would make the billing outcome easier to query, review, and explain from
the API side.

## Expected Evidence

- A read model representing generated or simulated invoice data.
- Tests proving that renewals produce visible billing history.
- API examples showing how a reviewer can inspect the result.
