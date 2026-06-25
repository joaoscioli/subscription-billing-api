package com.joaoscioli.billing.organizations;

public class DuplicateOrganizationSlugException extends RuntimeException {

    DuplicateOrganizationSlugException(String slug) {
        super("Organization slug already exists: " + slug);
    }
}
