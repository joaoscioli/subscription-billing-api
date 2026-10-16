package com.joaoscioli.billing.subscriptions;

import com.joaoscioli.billing.customers.Customer;
import com.joaoscioli.billing.organizations.Organization;
import com.joaoscioli.billing.plans.BillingInterval;
import com.joaoscioli.billing.plans.Plan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class SubscriptionRepositoryTests {
    @Autowired
    private SubscriptionRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void staleRenewalCannotOverwriteACancellation() {
        var stale = repository.saveAndFlush(subscriptionEndingOn(LocalDate.of(2026, 3, 1)));
        entityManager.clear();
        var current = repository.findById(stale.getId()).orElseThrow();
        current.cancel();
        repository.flush();
        entityManager.clear();

        var canceled = repository.findById(stale.getId()).orElseThrow();
        assertThat(canceled.getStatus()).isEqualTo(SubscriptionStatus.CANCELED);
        assertThat(canceled.getCurrentPeriodEnd()).isEqualTo(LocalDate.of(2026, 3, 1));
        stale.renew(LocalDate.of(2026, 4, 1));

        assertThatThrownBy(() -> repository.saveAndFlush(stale))
                .isInstanceOf(org.springframework.dao.OptimisticLockingFailureException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2026-01-31", "2026-02-01"})
    void databaseRejectsNonIncreasingPeriodsEvenWhenServiceValidationIsBypassed(String end) {
        var subscription = subscriptionEndingOn(LocalDate.parse(end));

        assertThatThrownBy(() -> repository.saveAndFlush(subscription))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasStackTraceContaining("CK_SUBSCRIPTIONS_PERIOD_ORDER");
    }

    @Test
    void databaseAcceptsOneDayPeriodAndPreservesDatesAfterReload() {
        var saved = repository.saveAndFlush(subscriptionEndingOn(LocalDate.of(2026, 2, 2)));
        entityManager.clear();

        var reloaded = repository.findById(saved.getId()).orElseThrow();

        assertThat(reloaded).extracting(Subscription::getStartsOn, Subscription::getCurrentPeriodStart,
                        Subscription::getCurrentPeriodEnd, Subscription::getStatus)
                .containsExactly(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 1),
                        LocalDate.of(2026, 2, 2), SubscriptionStatus.ACTIVE);
    }

    private Subscription subscriptionEndingOn(LocalDate end) {
        var organization = entityManager.persistAndFlush(new Organization("Acme", "period-acme"));
        var customer = entityManager.persistAndFlush(new Customer(organization, "Ada", "ada@acme.com"));
        var plan = entityManager.persistAndFlush(
                new Plan(organization, "Pro", "pro", 2500, "BRL", BillingInterval.MONTHLY));
        var start = LocalDate.of(2026, 2, 1);
        return new Subscription(organization, customer, plan, start, start, end);
    }
}
