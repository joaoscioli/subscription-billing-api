package com.joaoscioli.billing.subscriptions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubscriptionEventRepository extends JpaRepository<SubscriptionEvent, UUID> {
    long countBySubscriptionIdAndEventType(UUID subscriptionId, SubscriptionEventType eventType);
}
