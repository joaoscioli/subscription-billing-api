package com.joaoscioli.billing.plans;

import com.joaoscioli.billing.organizations.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class PlanRepositoryTests {
    @Autowired
    private PlanRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void databaseRejectsDuplicatePlanCodeWithinAnOrganization() {
        var organization = entityManager.persistAndFlush(new Organization("Acme", "plan-acme"));
        repository.saveAndFlush(new Plan(organization, "Pro", "pro", 2500, "BRL", BillingInterval.MONTHLY));
        entityManager.clear();

        // Exercise the database constraint independently of the service's precheck.
        assertThatThrownBy(() -> repository.saveAndFlush(
                new Plan(organization, "Another Pro", "pro", 5000, "BRL", BillingInterval.YEARLY)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void sameCodeAcrossOrganizationsKeepsIndependentPricesAndBillingIntervals() {
        var acme = entityManager.persistAndFlush(new Organization("Acme", "plan-acme"));
        var beta = entityManager.persistAndFlush(new Organization("Beta", "plan-beta"));
        var monthly = repository.saveAndFlush(new Plan(acme, "Pro", "pro", 2500, "BRL", BillingInterval.MONTHLY));
        var yearly = repository.saveAndFlush(new Plan(beta, "Pro", "pro", 24000, "USD", BillingInterval.YEARLY));
        entityManager.clear();

        var acmePlan = repository.findByCodeAndOrganization("pro", acme).orElseThrow();
        var betaPlan = repository.findByCodeAndOrganization("pro", beta).orElseThrow();
        assertThat(acmePlan).extracting(Plan::getId, Plan::getPriceCents, Plan::getCurrency, Plan::getBillingInterval)
                .containsExactly(monthly.getId(), 2500L, "BRL", BillingInterval.MONTHLY);
        assertThat(betaPlan).extracting(Plan::getId, Plan::getPriceCents, Plan::getCurrency, Plan::getBillingInterval)
                .containsExactly(yearly.getId(), 24000L, "USD", BillingInterval.YEARLY);
        assertThat(repository.findAllByOrganizationOrderByCreatedAtAsc(acme))
                .extracting(Plan::getId).containsExactly(monthly.getId());
        assertThat(repository.findAllByOrganizationOrderByCreatedAtAsc(beta))
                .extracting(Plan::getId).containsExactly(yearly.getId());
    }
}
