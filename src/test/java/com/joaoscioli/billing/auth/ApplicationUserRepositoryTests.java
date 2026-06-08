package com.joaoscioli.billing.auth;

import com.joaoscioli.billing.organizations.CreateOrganizationRequest;
import com.joaoscioli.billing.organizations.OrganizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
@Sql(
        statements = {
                "DELETE FROM application_users",
                "DELETE FROM subscriptions",
                "DELETE FROM plans",
                "DELETE FROM customers",
                "DELETE FROM organizations"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class ApplicationUserRepositoryTests {

    @Autowired
    private ApplicationUserRepository repository;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationUserDetailsService userDetailsService;

    @Test
    void savesApplicationUserWithEncodedPassword() {
        var organization = organizationService.create(new CreateOrganizationRequest("Acme Inc", "acme"));
        var passwordHash = passwordEncoder.encode("Str0ngPassword!");

        var user = repository.save(new ApplicationUser(
                organizationService.getBySlug(organization.slug()),
                "owner@acme.com",
                passwordHash,
                ApplicationUserRole.OWNER
        ));

        assertThat(user.getId()).isNotNull();
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
        assertThat(user.getPasswordHash()).isNotEqualTo("Str0ngPassword!");
        assertThat(passwordEncoder.matches("Str0ngPassword!", user.getPasswordHash())).isTrue();
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void findsApplicationUserByEmail() {
        var organization = organizationService.create(new CreateOrganizationRequest("Beta Labs", "beta-labs"));

        repository.save(new ApplicationUser(
                organizationService.getBySlug(organization.slug()),
                "admin@beta-labs.com",
                passwordEncoder.encode("Str0ngPassword!"),
                ApplicationUserRole.ADMIN
        ));

        var found = repository.findByEmail("admin@beta-labs.com");

        assertThat(found).isPresent();
        assertThat(found.orElseThrow().getRole()).isEqualTo(ApplicationUserRole.ADMIN);
        assertThat(repository.existsByEmail("admin@beta-labs.com")).isTrue();
    }

    @Test
    void loadsApplicationUserAsSpringSecurityUserDetails() {
        var organization = organizationService.create(new CreateOrganizationRequest("Gamma Ops", "gamma-ops"));

        repository.save(new ApplicationUser(
                organizationService.getBySlug(organization.slug()),
                "member@gamma-ops.com",
                passwordEncoder.encode("Str0ngPassword!"),
                ApplicationUserRole.MEMBER
        ));

        var userDetails = userDetailsService.loadUserByUsername("member@gamma-ops.com");

        assertThat(userDetails.getUsername()).isEqualTo("member@gamma-ops.com");
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_MEMBER");
        assertThat(userDetails.isEnabled()).isTrue();
    }

    @Test
    void rejectsUnknownApplicationUser() {
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("missing@example.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Application user not found: missing@example.com");
    }
}
