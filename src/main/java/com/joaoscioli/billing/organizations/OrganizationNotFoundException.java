package com.joaoscioli.billing.organizations;

public class OrganizationNotFoundException extends RuntimeException {

    OrganizationNotFoundException(String slug) {
        super("Organization not found: " + slug);
    }
}
