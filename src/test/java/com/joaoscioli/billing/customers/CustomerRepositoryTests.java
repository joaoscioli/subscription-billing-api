package com.joaoscioli.billing.customers;

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
class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void databaseRejectsDuplicateEmailWithinTheSameOrganization() {
        var organization = entityManager.persistAndFlush(new Organization("Acme", "unique-acme"));
        repository.saveAndFlush(new Customer(organization, "Ada", "ada@example.com"));
        entityManager.clear();

        // Bypass service prechecks: the migrated database must enforce this invariant too.
        assertThatThrownBy(() -> repository.saveAndFlush(
                new Customer(organization, "Another Ada", "ada@example.com")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void sameEmailInDifferentOrganizationsIsPersistedAndLookupStaysScoped() {
        var acme = entityManager.persistAndFlush(new Organization("Acme", "scoped-acme"));
        var beta = entityManager.persistAndFlush(new Organization("Beta", "scoped-beta"));
        var acmeCustomer = repository.saveAndFlush(new Customer(acme, "Ada", "ada@example.com"));
        var betaCustomer = repository.saveAndFlush(new Customer(beta, "Ada", "ada@example.com"));
        entityManager.clear();

        assertThat(repository.findByIdAndOrganization(acmeCustomer.getId(), beta)).isEmpty();
        assertThat(repository.findByIdAndOrganization(betaCustomer.getId(), acme)).isEmpty();
        assertThat(repository.findAllByOrganizationOrderByCreatedAtAsc(acme))
                .extracting(Customer::getId).containsExactly(acmeCustomer.getId());
        assertThat(repository.findAllByOrganizationOrderByCreatedAtAsc(beta))
                .extracting(Customer::getId).containsExactly(betaCustomer.getId());
    }
}
