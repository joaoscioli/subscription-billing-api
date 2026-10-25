package com.joaoscioli.billing.security;

import com.joaoscioli.billing.auth.ApplicationUser;
import com.joaoscioli.billing.auth.ApplicationUserRepository;
import com.joaoscioli.billing.organizations.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TenantAuthorizationManagerTest {
    private final ApplicationUserRepository repository = mock(ApplicationUserRepository.class);
    private final TenantAuthorizationManager manager = new TenantAuthorizationManager(repository);

    @Test
    void allowsOnlyMatchingOrganizationForEnabledUser() {
        var user = mock(ApplicationUser.class);
        var organization = mock(Organization.class);
        when(repository.findByEmail("owner@example.com")).thenReturn(Optional.of(user));
        when(user.isEnabled()).thenReturn(true);
        when(user.getOrganization()).thenReturn(organization);
        when(organization.getSlug()).thenReturn("acme");
        var authentication = new UsernamePasswordAuthenticationToken(
                "owner@example.com", "secret", java.util.List.of(() -> "ROLE_OWNER"));
        assertThat(manager.check(() -> authentication, context("acme")).isGranted()).isTrue();
        assertThat(manager.check(() -> authentication, context("other")).isGranted()).isFalse();
    }

    @Test
    void deniesMissingAndUnauthenticatedUsers() {
        var authentication = new UsernamePasswordAuthenticationToken(
                "unknown@example.com", "secret", java.util.List.of(() -> "ROLE_MEMBER"));
        assertThat(manager.check(() -> authentication, context("acme")).isGranted()).isFalse();
        assertThat(manager.check(() -> null, context("acme")).isGranted()).isFalse();
    }

    private RequestAuthorizationContext context(String slug) {
        return new RequestAuthorizationContext(new MockHttpServletRequest(), Map.of("organizationSlug", slug));
    }
}
