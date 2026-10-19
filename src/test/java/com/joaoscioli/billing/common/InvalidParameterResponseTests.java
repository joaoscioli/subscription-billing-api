package com.joaoscioli.billing.common;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class InvalidParameterResponseTests {
    @Test
    void invalidUuidAndIntegerReturnTheSameErrorEnvelopeBeforeBusinessWork() throws Exception {
        var business = mock(Runnable.class);
        var mvc = MockMvcBuilders.standaloneSetup(new LookupController(business))
                .setControllerAdvice(new GlobalExceptionHandler()).build();

        for (var path : new String[]{"/lookup/not-a-uuid", "/lookup/" + UUID.randomUUID() + "?limit=not-a-number"}) {
            mvc.perform(get(path).header("X-Correlation-Id", "lookup-123"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("Request parameter has an invalid value or type"))
                    .andExpect(jsonPath("$.correlationId").value("lookup-123"))
                    .andExpect(jsonPath("$.details").isEmpty());
        }
        verifyNoInteractions(business);

        mvc.perform(get("/lookup/00000000-0000-0000-0000-000000000001?limit=1"))
                .andExpect(status().isOk());
        verify(business).run();
        verifyNoMoreInteractions(business);
    }

    @RestController
    static class LookupController {
        private final Runnable business;
        LookupController(Runnable business) { this.business = business; }

        @GetMapping("/lookup/{id}")
        void lookup(@PathVariable("id") UUID id, @RequestParam(name = "limit", defaultValue = "10") int limit) {
            business.run();
        }
    }
}
