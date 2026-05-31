package com.joaoscioli.billing.customers;

import com.joaoscioli.billing.organizations.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface CustomerRepository extends JpaRepository<Customer, UUID> {

    boolean existsByOrganizationAndEmail(Organization organization, String email);

    List<Customer> findAllByOrganizationOrderByCreatedAtAsc(Organization organization);

    Optional<Customer> findByIdAndOrganization(UUID id, Organization organization);
}
