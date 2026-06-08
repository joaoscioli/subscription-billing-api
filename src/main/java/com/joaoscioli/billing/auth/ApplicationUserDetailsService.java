package com.joaoscioli.billing.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
class ApplicationUserDetailsService implements UserDetailsService {

    private final ApplicationUserRepository repository;

    ApplicationUserDetailsService(ApplicationUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Application user not found: " + username));

        return User.withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .roles(user.getRole().name())
                .disabled(!user.isEnabled())
                .build();
    }
}
