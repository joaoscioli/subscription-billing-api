# Production Readiness Matrix

This matrix separates portfolio readiness from production readiness.

| Capability | Current State | Production Expectation |
| --- | --- | --- |
| API contract | Documented and reviewable. | Versioning policy and backward-compatibility tests. |
| Persistence | Planned around subscription lifecycle data. | Migration strategy, backup policy, and data retention rules. |
| Security | Authentication and authorization are mapped. | Enforced permissions, secret rotation, and audit trails. |
| Observability | Key signals are documented. | Dashboards, alerts, SLOs, and incident runbooks. |

## Review Note

The project is portfolio-ready when it explains the path to production clearly.
It becomes production-ready when those controls are implemented and operated.
