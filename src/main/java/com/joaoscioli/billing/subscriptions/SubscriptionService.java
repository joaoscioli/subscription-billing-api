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
    private final SubscriptionEventRepository eventRepository;
    private final OrganizationService organizationService;
    private final CustomerService customerService;
    private final PlanService planService;

    SubscriptionService(
            SubscriptionRepository repository,
            SubscriptionEventRepository eventRepository,
            OrganizationService organizationService,
            CustomerService customerService,
            PlanService planService
    ) {
        this.repository = repository;
        this.eventRepository = eventRepository;
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
        recordEvent(saved, SubscriptionEventType.CREATED, "Subscription created");

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

    @Transactional(readOnly = true)
    public List<SubscriptionEventResponse> listEvents(String organizationSlug, UUID id) {
        var subscription = getById(organizationSlug, id);

        return eventRepository.findAllBySubscription_IdOrderByOccurredAtAsc(subscription.getId())
                .stream()
                .map(SubscriptionEventResponse::from)
                .toList();
    }

    @Transactional
    public SubscriptionResponse cancel(String organizationSlug, UUID id) {
        var subscription = getById(organizationSlug, id);
        var wasActive = subscription.getStatus() == SubscriptionStatus.ACTIVE;
        subscription.cancel();
        if (wasActive) {
            recordEvent(subscription, SubscriptionEventType.CANCELED, "Subscription canceled");
        }

        return SubscriptionResponse.from(subscription);
    }

    @Transactional
    public SubscriptionResponse renew(String organizationSlug, UUID id) {
        var subscription = getById(organizationSlug, id);
        var nextPeriodEnd = calculateCurrentPeriodEnd(
                subscription.getCurrentPeriodEnd(),
                subscription.getPlan().getBillingInterval()
        );
        subscription.renew(nextPeriodEnd);
        recordEvent(subscription, SubscriptionEventType.RENEWED, "Subscription renewed until " + nextPeriodEnd);

        return SubscriptionResponse.from(subscription);
    }

    private void recordEvent(Subscription subscription, SubscriptionEventType eventType, String description) {
        eventRepository.save(new SubscriptionEvent(subscription, eventType, description));
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
