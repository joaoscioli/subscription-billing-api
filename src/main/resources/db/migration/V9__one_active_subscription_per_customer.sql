-- Enforce invariant across concurrent transactions.
CREATE UNIQUE INDEX uk_subscriptions_one_active_per_customer
ON subscriptions (customer_id)
WHERE status = 'ACTIVE';
