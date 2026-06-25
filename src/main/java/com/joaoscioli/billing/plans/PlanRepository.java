package com.joaoscioli.billing.plans;

import com.joaoscioli.billing.organizations.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface PlanRepository extends JpaRepository<Plan, UUID> {

    boolean existsByOrganizationAndCode(Organization organization, String code);

    List<Plan> findAllByOrganizationOrderByCreatedAtAsc(Organization organization);

    Optional<Plan> findByCodeAndOrganization(String code, Organization organization);
}
