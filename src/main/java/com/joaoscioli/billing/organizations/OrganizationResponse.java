package com.joaoscioli.billing.organizations;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OrganizationResponse(
        UUID id,
        String name,
        String slug,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    static OrganizationResponse from(Organization organization) {
        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getSlug(),
                organization.getCreatedAt(),
                organization.getUpdatedAt()
        );
    }
}
