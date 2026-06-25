package com.joaoscioli.billing.customers;

import com.joaoscioli.billing.organizations.OrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final OrganizationService organizationService;

    CustomerService(CustomerRepository repository, OrganizationService organizationService) {
        this.repository = repository;
        this.organizationService = organizationService;
    }

    @Transactional
    public CustomerResponse create(String organizationSlug, CreateCustomerRequest request) {
        var organization = organizationService.getBySlug(organizationSlug);
        var email = normalizeEmail(request.email());

        if (repository.existsByOrganizationAndEmail(organization, email)) {
            throw new DuplicateCustomerEmailException(email);
        }

        var customer = new Customer(organization, request.name().trim(), email);
        var saved = repository.save(customer);

        return CustomerResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> list(String organizationSlug) {
        var organization = organizationService.getBySlug(organizationSlug);

        return repository.findAllByOrganizationOrderByCreatedAtAsc(organization)
                .stream()
                .map(CustomerResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(String organizationSlug, UUID id) {
        return CustomerResponse.from(getById(organizationSlug, id));
    }

    @Transactional(readOnly = true)
    public Customer getById(String organizationSlug, UUID id) {
        var organization = organizationService.getBySlug(organizationSlug);

        return repository.findByIdAndOrganization(id, organization)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
