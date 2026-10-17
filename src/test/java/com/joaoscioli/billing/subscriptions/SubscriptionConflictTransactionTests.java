package com.joaoscioli.billing.subscriptions;

import com.joaoscioli.billing.customers.Customer;
import com.joaoscioli.billing.organizations.Organization;
import com.joaoscioli.billing.plans.BillingInterval;
import com.joaoscioli.billing.plans.Plan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SubscriptionConflictTransactionTests {
    @Autowired SubscriptionRepository subscriptions;
    @Autowired SubscriptionEventRepository events;
    @PersistenceContext EntityManager entityManager;
    @Autowired PlatformTransactionManager transactionManager;

    @Test
    void failedStaleRenewalRollsBackItsAuditEventAndPreservesCommittedCancellation() {
        var transaction = new TransactionTemplate(transactionManager);
        var id = transaction.execute(status -> {
            var organization = new Organization("Conflict test", "conflict-" + UUID.randomUUID());
            entityManager.persist(organization);
            var customer = new Customer(organization, "Ada", "ada@example.com");
            entityManager.persist(customer);
            var plan = new Plan(organization, "Pro", "pro", 2500, "BRL", BillingInterval.MONTHLY);
            entityManager.persist(plan);
            var start = LocalDate.of(2026, 2, 1);
            return subscriptions.save(new Subscription(organization, customer, plan, start, start,
                    LocalDate.of(2026, 3, 1))).getId();
        });
        // Separate transactions give us a genuinely detached, stale version.
        var stale = transaction.execute(status -> subscriptions.findById(id).orElseThrow());
        transaction.executeWithoutResult(status -> {
            var current = subscriptions.findById(id).orElseThrow();
            current.cancel();
            events.save(new SubscriptionEvent(current, SubscriptionEventType.CANCELED, "Committed cancellation"));
        });

        assertThatThrownBy(() -> transaction.executeWithoutResult(status -> {
            // Flush an audit INSERT first to prove database rollback, not just a skipped save.
            events.saveAndFlush(new SubscriptionEvent(subscriptions.getReferenceById(id),
                    SubscriptionEventType.RENEWED, "Must roll back"));
            stale.renew(LocalDate.of(2026, 4, 1));
            subscriptions.saveAndFlush(stale);
        })).isInstanceOf(OptimisticLockingFailureException.class);

        transaction.executeWithoutResult(status -> {
            var current = subscriptions.findById(id).orElseThrow();
            assertThat(current.getStatus()).isEqualTo(SubscriptionStatus.CANCELED);
            assertThat(current.getCanceledAt()).isNotNull();
            assertThat(current.getCurrentPeriodEnd()).isEqualTo(LocalDate.of(2026, 3, 1));
            assertThat(events.findAllBySubscription_IdOrderByOccurredAtAsc(id))
                    .extracting(SubscriptionEvent::getEventType)
                    .containsExactly(SubscriptionEventType.CANCELED);
        });
    }
}
