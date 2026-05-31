package com.joaoscioli.billing.customers;

public class DuplicateCustomerEmailException extends RuntimeException {

    DuplicateCustomerEmailException(String email) {
        super("Customer email already exists for organization: " + email);
    }
}
