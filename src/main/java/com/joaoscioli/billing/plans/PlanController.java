package com.joaoscioli.billing.plans;

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

@RestController
@RequestMapping("/api/organizations/{organizationSlug}/plans")
public class PlanController {

    private final PlanService service;

    PlanController(PlanService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanResponse create(
            @PathVariable String organizationSlug,
            @RequestBody @Valid CreatePlanRequest request
    ) {
        return service.create(organizationSlug, request);
    }

    @GetMapping
    public List<PlanResponse> list(@PathVariable String organizationSlug) {
        return service.list(organizationSlug);
    }

    @GetMapping("/{code}")
    public PlanResponse findByCode(@PathVariable String organizationSlug, @PathVariable String code) {
        return service.findByCode(organizationSlug, code);
    }
}
