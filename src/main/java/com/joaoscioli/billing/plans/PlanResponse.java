package com.joaoscioli.billing.plans;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PlanResponse(
        UUID id,
        UUID organizationId,
        String organizationSlug,
        String name,
        String code,
        long priceCents,
        String currency,
        BillingInterval billingInterval,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    static PlanResponse from(Plan plan) {
        return new PlanResponse(
                plan.getId(),
                plan.getOrganization().getId(),
                plan.getOrganization().getSlug(),
                plan.getName(),
                plan.getCode(),
                plan.getPriceCents(),
                plan.getCurrency(),
                plan.getBillingInterval(),
                plan.getCreatedAt(),
                plan.getUpdatedAt()
        );
    }
}
