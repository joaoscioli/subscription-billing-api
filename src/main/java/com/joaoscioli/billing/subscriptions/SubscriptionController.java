package com.joaoscioli.billing.subscriptions;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations/{organizationSlug}/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponse create(
            @PathVariable String organizationSlug,
            @RequestBody @Valid CreateSubscriptionRequest request
    ) {
        return service.create(organizationSlug, request);
    }

    @GetMapping
    public List<SubscriptionResponse> list(@PathVariable String organizationSlug) {
        return service.list(organizationSlug);
    }

    @GetMapping("/{id}")
    public SubscriptionResponse findById(@PathVariable String organizationSlug, @PathVariable UUID id) {
        return service.findById(organizationSlug, id);
    }

    @GetMapping("/{id}/events")
    public List<SubscriptionEventResponse> listEvents(@PathVariable String organizationSlug, @PathVariable UUID id) {
        return service.listEvents(organizationSlug, id);
    }

    @PostMapping("/{id}/cancel")
    public SubscriptionResponse cancel(@PathVariable String organizationSlug, @PathVariable UUID id) {
        return service.cancel(organizationSlug, id);
    }

    @PostMapping("/{id}/renew")
    public SubscriptionResponse renew(@PathVariable String organizationSlug, @PathVariable UUID id) {
        return service.renew(organizationSlug, id);
    }
}
