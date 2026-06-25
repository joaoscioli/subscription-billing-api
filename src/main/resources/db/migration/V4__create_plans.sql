CREATE TABLE plans (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    name VARCHAR(120) NOT NULL,
    code VARCHAR(80) NOT NULL,
    price_cents BIGINT NOT NULL,
    currency VARCHAR(3) NOT NULL,
    billing_interval VARCHAR(30) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_plans_organization FOREIGN KEY (organization_id) REFERENCES organizations(id),
    CONSTRAINT uk_plans_organization_code UNIQUE (organization_id, code),
    CONSTRAINT ck_plans_price_cents_positive CHECK (price_cents > 0)
);

CREATE INDEX idx_plans_organization_id ON plans(organization_id);
