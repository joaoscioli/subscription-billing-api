# Decision Log

This log summarizes important technical choices in a review-friendly format.

## Decisions

| Decision | Reason | Trade-off |
| --- | --- | --- |
| Start as a modular monolith. | Keeps subscription, billing, and API behavior easy to review locally. | Service boundaries are documented before distributed deployment. |
| Keep payment integration behind a boundary. | Protects the domain from provider-specific APIs. | Real provider behavior still needs adapter tests. |
| Document production gaps explicitly. | Makes roadmap and risk visible to reviewers. | The project exposes unfinished areas instead of looking artificially complete. |

## Interview Use

Use this file to explain how each architecture choice supports clarity, delivery
speed, and future production hardening.
