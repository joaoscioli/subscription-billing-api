package com.joaoscioli.billing.subscriptions;

import java.util.UUID;

public class SubscriptionNotFoundException extends RuntimeException {

    SubscriptionNotFoundException(UUID id) {
        super("Subscription not found: " + id);
    }
}
