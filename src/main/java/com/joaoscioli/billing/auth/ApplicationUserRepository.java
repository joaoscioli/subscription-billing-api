package com.joaoscioli.billing.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface ApplicationUserRepository extends JpaRepository<ApplicationUser, UUID> {

    boolean existsByEmail(String email);

    Optional<ApplicationUser> findByEmail(String email);
}
