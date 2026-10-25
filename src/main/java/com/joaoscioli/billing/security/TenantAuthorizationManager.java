package com.joaoscioli.billing.security;

import com.joaoscioli.billing.auth.ApplicationUser;
import com.joaoscioli.billing.auth.ApplicationUserRepository;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class TenantAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final ApplicationUserRepository users;

    public TenantAuthorizationManager(ApplicationUserRepository users) {
        this.users = users;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        var auth = authentication.get();
        if (auth == null || !auth.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }
        var slug = context.getVariables().get("organizationSlug");
        if (slug == null) {
            return new AuthorizationDecision(false);
        }
        boolean allowed = users.findByEmail(auth.getName())
                .filter(ApplicationUser::isEnabled)
                .map(user -> slug.equals(user.getOrganization().getSlug()))
                .orElse(false);
        return new AuthorizationDecision(allowed);
    }
}
