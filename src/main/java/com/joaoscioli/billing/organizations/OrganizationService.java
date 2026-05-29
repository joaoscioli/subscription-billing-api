package com.joaoscioli.billing.organizations;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrganizationService {

    private final OrganizationRepository repository;

    OrganizationService(OrganizationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public OrganizationResponse create(CreateOrganizationRequest request) {
        if (repository.existsBySlug(request.slug())) {
            throw new DuplicateOrganizationSlugException(request.slug());
        }

        var organization = new Organization(request.name(), request.slug());
        var saved = repository.save(organization);

        return OrganizationResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> list() {
        return repository.findAll()
                .stream()
                .map(OrganizationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrganizationResponse findBySlug(String slug) {
        return repository.findBySlug(slug)
                .map(OrganizationResponse::from)
                .orElseThrow(() -> new OrganizationNotFoundException(slug));
    }
}
