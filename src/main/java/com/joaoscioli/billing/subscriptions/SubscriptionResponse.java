package com.joaoscioli.billing.subscriptions;

import com.joaoscioli.billing.plans.BillingInterval;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record SubscriptionResponse(
        UUID id,
        UUID organizationId,
        String organizationSlug,
        UUID customerId,
        String customerName,
        UUID planId,
        String planCode,
        BillingInterval billingInterval,
        SubscriptionStatus status,
        LocalDate startsOn,
        LocalDate currentPeriodStart,
        LocalDate currentPeriodEnd,
        OffsetDateTime canceledAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    static SubscriptionResponse from(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.getId(),
                subscription.getOrganization().getId(),
                subscription.getOrganization().getSlug(),
                subscription.getCustomer().getId(),
                subscription.getCustomer().getName(),
                subscription.getPlan().getId(),
                subscription.getPlan().getCode(),
                subscription.getPlan().getBillingInterval(),
                subscription.getStatus(),
                subscription.getStartsOn(),
                subscription.getCurrentPeriodStart(),
                subscription.getCurrentPeriodEnd(),
                subscription.getCanceledAt(),
                subscription.getCreatedAt(),
                subscription.getUpdatedAt()
        );
    }
}
