-- Prevent a stale renewal or cancellation from silently overwriting another update.
ALTER TABLE subscriptions ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
