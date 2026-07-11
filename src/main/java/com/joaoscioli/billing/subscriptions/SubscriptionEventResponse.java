package com.joaoscioli.billing.subscriptions;

import java.time.OffsetDateTime;
import java.util.UUID;

public record SubscriptionEventResponse(
        UUID id,
        UUID subscriptionId,
        SubscriptionEventType eventType,
        String description,
        OffsetDateTime occurredAt
) {
    static SubscriptionEventResponse from(SubscriptionEvent event) {
        return new SubscriptionEventResponse(
                event.getId(),
                event.getSubscriptionId(),
                event.getEventType(),
                event.getDescription(),
                event.getOccurredAt()
        );
    }
}
