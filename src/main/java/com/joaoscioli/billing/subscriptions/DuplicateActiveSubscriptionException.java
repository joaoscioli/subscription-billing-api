package com.joaoscioli.billing.subscriptions;

import java.util.UUID;

public class DuplicateActiveSubscriptionException extends RuntimeException {

    DuplicateActiveSubscriptionException(UUID customerId) {
        super("Customer already has an active subscription: " + customerId);
    }
}
