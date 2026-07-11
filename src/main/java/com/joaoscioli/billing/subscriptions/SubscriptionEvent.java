package com.joaoscioli.billing.subscriptions;

import com.joaoscioli.billing.organizations.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(name = "subscription_events")
public class SubscriptionEvent {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SubscriptionEventType eventType;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private OffsetDateTime occurredAt;

    protected SubscriptionEvent() {
    }

    public SubscriptionEvent(Subscription subscription, SubscriptionEventType eventType, String description) {
        this.id = UUID.randomUUID();
        this.organization = subscription.getOrganization();
        this.subscription = subscription;
        this.eventType = eventType;
        this.description = description;
    }

    @PrePersist
    void prePersist() {
        this.occurredAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public UUID getId() {
        return id;
    }

    public UUID getSubscriptionId() {
        return subscription.getId();
    }

    public SubscriptionEventType getEventType() {
        return eventType;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }
}
