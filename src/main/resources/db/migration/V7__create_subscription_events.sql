CREATE TABLE subscription_events (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    subscription_id UUID NOT NULL,
    event_type VARCHAR(30) NOT NULL,
    description VARCHAR(500) NOT NULL,
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_subscription_events_organization FOREIGN KEY (organization_id) REFERENCES organizations(id),
    CONSTRAINT fk_subscription_events_subscription FOREIGN KEY (subscription_id) REFERENCES subscriptions(id)
);

CREATE INDEX idx_subscription_events_organization_id ON subscription_events(organization_id);
CREATE INDEX idx_subscription_events_subscription_id ON subscription_events(subscription_id);
