CREATE TABLE customers (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(254) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_customers_organization FOREIGN KEY (organization_id) REFERENCES organizations(id),
    CONSTRAINT uk_customers_organization_email UNIQUE (organization_id, email)
);

CREATE INDEX idx_customers_organization_id ON customers(organization_id);
