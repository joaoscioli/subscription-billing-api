package com.joaoscioli.billing.customers;

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
@RequestMapping("/api/organizations/{organizationSlug}/customers")
public class CustomerController {

    private final CustomerService service;

    CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(
            @PathVariable String organizationSlug,
            @RequestBody @Valid CreateCustomerRequest request
    ) {
        return service.create(organizationSlug, request);
    }

    @GetMapping
    public List<CustomerResponse> list(@PathVariable String organizationSlug) {
        return service.list(organizationSlug);
    }

    @GetMapping("/{id}")
    public CustomerResponse findById(@PathVariable String organizationSlug, @PathVariable UUID id) {
        return service.findById(organizationSlug, id);
    }
}
