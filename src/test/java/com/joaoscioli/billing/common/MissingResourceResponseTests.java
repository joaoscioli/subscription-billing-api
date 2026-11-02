package com.joaoscioli.billing.common;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;

class MissingResourceResponseTests {
    @Test
    void missingResourceHasCorrelatedErrorEnvelopeWithoutInternalResourcePath() throws Exception {
        var mvc = MockMvcBuilders.standaloneSetup(new ResourceController())
                .setControllerAdvice(new GlobalExceptionHandler()).build();
        mvc.perform(get("/missing").header("X-Correlation-Id", "missing-123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Requested resource was not found"))
                .andExpect(jsonPath("$.path").value("/missing"))
                .andExpect(jsonPath("$.correlationId").value("missing-123"))
                .andExpect(jsonPath("$.details").isEmpty())
                .andExpect(content().string(not(containsString("internal-resource"))));
    }

    @RestController
    static class ResourceController {
        @GetMapping("/missing")
        void missing() throws NoResourceFoundException {
            throw new NoResourceFoundException(HttpMethod.GET, "internal-resource");
        }
    }
}
