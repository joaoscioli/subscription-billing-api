package com.joaoscioli.billing.subscriptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"2026-01-31", "2026-02-01"})
    void rejectedRenewalPreservesTheActiveBillingPeriod(String nextEnd) {
        var subscription = subscriptionEndingOn(LocalDate.of(2026, 2, 1));

        var exception = assertThrows(SubscriptionStateException.class,
                () -> subscription.renew(nextEnd == null ? null : LocalDate.parse(nextEnd)));

        assertEquals("Renewal period end must be after the current period end", exception.getMessage());
        assertEquals(LocalDate.of(2026, 1, 1), subscription.getCurrentPeriodStart());
        assertEquals(LocalDate.of(2026, 2, 1), subscription.getCurrentPeriodEnd());
        assertEquals(SubscriptionStatus.ACTIVE, subscription.getStatus());
    }

    @Test
    void canceledSubscriptionCannotRenewOrLoseItsCancellationTimestamp() {
        var subscription = subscriptionEndingOn(LocalDate.of(2026, 2, 1));
        subscription.cancel();
        var canceledAt = subscription.getCanceledAt();

        var exception = assertThrows(SubscriptionStateException.class,
                () -> subscription.renew(LocalDate.of(2026, 3, 1)));

        assertEquals("Only active subscriptions can be renewed", exception.getMessage());
        assertEquals(SubscriptionStatus.CANCELED, subscription.getStatus());
        assertEquals(canceledAt, subscription.getCanceledAt());
        assertEquals(LocalDate.of(2026, 1, 1), subscription.getCurrentPeriodStart());
        assertEquals(LocalDate.of(2026, 2, 1), subscription.getCurrentPeriodEnd());
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
