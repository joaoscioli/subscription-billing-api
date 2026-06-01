package com.joaoscioli.billing.subscriptions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record CreateSubscriptionRequest(
        @NotNull
        UUID customerId,

        @NotBlank
        @Size(max = 80)
        @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$")
        String planCode,

        @NotNull
        LocalDate startsOn
) {
}
