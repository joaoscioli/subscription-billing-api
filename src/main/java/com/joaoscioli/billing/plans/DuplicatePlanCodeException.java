package com.joaoscioli.billing.plans;

public class DuplicatePlanCodeException extends RuntimeException {

    DuplicatePlanCodeException(String code) {
        super("Plan code already exists for organization: " + code);
    }
}
