CREATE TABLE application_users (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    email VARCHAR(254) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL,
    enabled BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_application_users_organization FOREIGN KEY (organization_id) REFERENCES organizations(id),
    CONSTRAINT uk_application_users_email UNIQUE (email)
);

CREATE INDEX idx_application_users_organization_id ON application_users(organization_id);
