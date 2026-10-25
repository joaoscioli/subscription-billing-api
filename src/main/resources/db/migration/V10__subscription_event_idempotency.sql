ALTER TABLE subscription_events ADD COLUMN idempotency_key VARCHAR(128);
CREATE UNIQUE INDEX uk_subscription_events_idempotency ON subscription_events (subscription_id, idempotency_key);
