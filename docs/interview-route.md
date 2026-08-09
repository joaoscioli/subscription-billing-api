# Interview Route

Use this route to navigate the repository during a technical interview.

## Start Here

Open the README and explain the product scope: a SaaS billing backend focused on
organizations, customers, plans, subscriptions, and simulated billing behavior.

## Show The Core Slice

Open the subscription lifecycle code and tests. Explain how creation, renewal,
cancellation, persistence, API contracts, and audit events work together.

## Discuss Engineering Judgment

Use the ADRs, test strategy, observability notes, and quality gates to show that
the project is designed for reviewability and future production hardening.

## Close With The Next Step

Explain that the next meaningful improvement is connecting renewals to invoice
read models so the billing history becomes easier to query.
