package com.joaoscioli.billing.subscriptions;

import com.joaoscioli.billing.customers.CustomerService;
import com.joaoscioli.billing.organizations.OrganizationService;
import com.joaoscioli.billing.plans.BillingInterval;
import com.joaoscioli.billing.plans.PlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;
    private final OrganizationService organizationService;
    private final CustomerService customerService;
    private final PlanService planService;

    SubscriptionService(
            SubscriptionRepository repository,
            OrganizationService organizationService,
            CustomerService customerService,
            PlanService planService
    ) {
        this.repository = repository;
        this.organizationService = organizationService;
        this.customerService = customerService;
        this.planService = planService;
    }

    @Transactional
    public SubscriptionResponse create(String organizationSlug, CreateSubscriptionRequest request) {
        var customer = customerService.getById(organizationSlug, request.customerId());
        var plan = planService.getByCode(organizationSlug, request.planCode().trim());

        if (repository.existsByCustomerAndStatus(customer, SubscriptionStatus.ACTIVE)) {
            throw new DuplicateActiveSubscriptionException(customer.getId());
        }

        var startsOn = request.startsOn();
        var subscription = new Subscription(
                customer.getOrganization(),
                customer,
                plan,
                startsOn,
                startsOn,
                calculateCurrentPeriodEnd(startsOn, plan.getBillingInterval())
        );
        var saved = repository.save(subscription);

        return SubscriptionResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<SubscriptionResponse> list(String organizationSlug) {
        var organization = organizationService.getBySlug(organizationSlug);

        return repository.findAllByOrganizationOrderByCreatedAtAsc(organization)
                .stream()
                .map(SubscriptionResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public SubscriptionResponse findById(String organizationSlug, UUID id) {
        return SubscriptionResponse.from(getById(organizationSlug, id));
    }

    @Transactional
    public SubscriptionResponse cancel(String organizationSlug, UUID id) {
        var subscription = getById(organizationSlug, id);
        subscription.cancel();

        return SubscriptionResponse.from(subscription);
    }

    private Subscription getById(String organizationSlug, UUID id) {
        var organization = organizationService.getBySlug(organizationSlug);

        return repository.findByIdAndOrganization(id, organization)
                .orElseThrow(() -> new SubscriptionNotFoundException(id));
    }

    private LocalDate calculateCurrentPeriodEnd(LocalDate startsOn, BillingInterval billingInterval) {
        return switch (billingInterval) {
            case MONTHLY -> startsOn.plusMonths(1);
            case YEARLY -> startsOn.plusYears(1);
        };
    }
}
