package com.joaoscioli.billing.subscriptions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionEventRepository extends JpaRepository<SubscriptionEvent, UUID> {
    boolean existsBySubscription_IdAndEventTypeAndIdempotencyKey(UUID subscriptionId, SubscriptionEventType eventType, String idempotencyKey);

    long countBySubscription_IdAndEventType(UUID subscriptionId, SubscriptionEventType eventType);

    List<SubscriptionEvent> findAllBySubscription_IdOrderByOccurredAtAsc(UUID subscriptionId);
}
