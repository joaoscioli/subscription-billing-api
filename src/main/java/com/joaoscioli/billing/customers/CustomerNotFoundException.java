package com.joaoscioli.billing.customers;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {

    CustomerNotFoundException(UUID id) {
        super("Customer not found: " + id);
    }
}
