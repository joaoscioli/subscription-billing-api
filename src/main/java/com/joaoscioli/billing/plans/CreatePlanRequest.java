package com.joaoscioli.billing.plans;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreatePlanRequest(
        @NotBlank
        @Size(max = 120)
        String name,

        @NotBlank
        @Size(max = 80)
        @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$")
        String code,

        @Min(1)
        long priceCents,

        @NotBlank
        @Size(min = 3, max = 3)
        @Pattern(regexp = "^[A-Za-z]{3}$")
        String currency,

        @NotNull
        BillingInterval billingInterval
) {
}
