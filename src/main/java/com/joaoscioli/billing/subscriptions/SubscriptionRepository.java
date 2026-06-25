package com.joaoscioli.billing.subscriptions;

import com.joaoscioli.billing.customers.Customer;
import com.joaoscioli.billing.organizations.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    boolean existsByCustomerAndStatus(Customer customer, SubscriptionStatus status);

    List<Subscription> findAllByOrganizationOrderByCreatedAtAsc(Organization organization);

    Optional<Subscription> findByIdAndOrganization(UUID id, Organization organization);
}
