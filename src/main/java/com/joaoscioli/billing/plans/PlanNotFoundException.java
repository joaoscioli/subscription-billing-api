package com.joaoscioli.billing.plans;

public class PlanNotFoundException extends RuntimeException {

    PlanNotFoundException(String code) {
        super("Plan not found: " + code);
    }
}
