# Technical Debt Register

This register makes project trade-offs visible instead of hiding unfinished work.

## Current Debt

| Area | Debt | Impact | Next Action |
| --- | --- | --- | --- |
| Payments | No real payment provider integration yet. | Billing behavior cannot be validated end to end. | Add a provider adapter behind a stable port. |
| Security | Authorization model is documented before full enforcement. | Sensitive subscription operations need stronger protection. | Add role-based tests for lifecycle commands. |
| Observability | Metrics are planned but not fully implemented. | Production diagnosis would be limited. | Add counters for creation, cancellation, renewal, and failures. |

## Review Rule

Debt is acceptable when it is explicit, bounded, and connected to a follow-up
decision. Hidden debt is what damages engineering trust.
