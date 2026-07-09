package com.joaoscioli.billing.subscriptions;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubscriptionTest {

    @Test
    void renewAdvancesTheBillingPeriod() {
        var subscription = subscriptionEndingOn(LocalDate.of(2026, 2, 1));

        subscription.renew(LocalDate.of(2026, 3, 1));

        assertEquals(LocalDate.of(2026, 2, 1), subscription.getCurrentPeriodStart());
        assertEquals(LocalDate.of(2026, 3, 1), subscription.getCurrentPeriodEnd());
    }

    @Test
    void renewRejectsPeriodEndThatDoesNotAdvance() {
        var subscription = subscriptionEndingOn(LocalDate.of(2026, 2, 1));

        assertThrows(
                SubscriptionStateException.class,
                () -> subscription.renew(LocalDate.of(2026, 2, 1))
        );
    }

    private Subscription subscriptionEndingOn(LocalDate periodEnd) {
        return new Subscription(
                null,
                null,
                null,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 1),
                periodEnd
        );
    }
}
