package com.joaoscioli.billing.plans;

import com.joaoscioli.billing.organizations.OrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class PlanService {

    private final PlanRepository repository;
    private final OrganizationService organizationService;

    PlanService(PlanRepository repository, OrganizationService organizationService) {
        this.repository = repository;
        this.organizationService = organizationService;
    }

    @Transactional
    public PlanResponse create(String organizationSlug, CreatePlanRequest request) {
        var organization = organizationService.getBySlug(organizationSlug);
        var code = request.code().trim();
        var currency = request.currency().trim().toUpperCase(Locale.ROOT);

        if (repository.existsByOrganizationAndCode(organization, code)) {
            throw new DuplicatePlanCodeException(code);
        }

        var plan = new Plan(
                organization,
                request.name().trim(),
                code,
                request.priceCents(),
                currency,
                request.billingInterval()
        );
        var saved = repository.save(plan);

        return PlanResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<PlanResponse> list(String organizationSlug) {
        var organization = organizationService.getBySlug(organizationSlug);

        return repository.findAllByOrganizationOrderByCreatedAtAsc(organization)
                .stream()
                .map(PlanResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlanResponse findByCode(String organizationSlug, String code) {
        return PlanResponse.from(getByCode(organizationSlug, code));
    }

    @Transactional(readOnly = true)
    public Plan getByCode(String organizationSlug, String code) {
        var organization = organizationService.getBySlug(organizationSlug);

        return repository.findByCodeAndOrganization(code, organization)
                .orElseThrow(() -> new PlanNotFoundException(code));
    }
}
