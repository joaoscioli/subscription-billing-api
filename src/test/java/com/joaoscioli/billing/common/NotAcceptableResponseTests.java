package com.joaoscioli.billing.common;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NotAcceptableResponseTests {
    @Test
    void unsupportedAcceptReturnsJsonErrorBeforeBusinessWorkAndAllowsLaterJsonRequest() throws Exception {
        var business = mock(Runnable.class);
        var mvc = MockMvcBuilders.standaloneSetup(new JsonController(business))
                .setControllerAdvice(new GlobalExceptionHandler()).build();

        mvc.perform(get("/json-only").accept(MediaType.APPLICATION_XML)
                        .header("X-Correlation-Id", "negotiation-123"))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(406))
                .andExpect(jsonPath("$.error").value("Not Acceptable"))
                .andExpect(jsonPath("$.message").value("Requested response media type is not supported"))
                .andExpect(jsonPath("$.path").value("/json-only"))
                .andExpect(jsonPath("$.correlationId").value("negotiation-123"))
                .andExpect(jsonPath("$.details").isEmpty());
        verifyNoInteractions(business);

        mvc.perform(get("/json-only").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ready"));
        verify(business).run();
        verifyNoMoreInteractions(business);
    }

    @RestController
    static class JsonController {
        private final Runnable business;
        JsonController(Runnable business) { this.business = business; }

        @GetMapping(value = "/json-only", produces = MediaType.APPLICATION_JSON_VALUE)
        Map<String, String> status() {
            business.run();
            return Map.of("status", "ready");
        }
    }
}
