package com.joaoscioli.billing.common;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UnsupportedMethodResponseTests {
    @Test
    void unsupportedMethodPreservesAllowHeaderAndStandardErrorWithoutBusinessWork() throws Exception {
        var business = mock(Runnable.class);
        var mvc = MockMvcBuilders.standaloneSetup(new ReadController(business))
                .setControllerAdvice(new GlobalExceptionHandler()).build();

        mvc.perform(post("/read-only").header("X-Correlation-Id", "method-123"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(header().string("Allow", containsString("GET")))
                .andExpect(jsonPath("$.status").value(405))
                .andExpect(jsonPath("$.error").value("Method Not Allowed"))
                .andExpect(jsonPath("$.message").value("HTTP method is not supported for this endpoint"))
                .andExpect(jsonPath("$.path").value("/read-only"))
                .andExpect(jsonPath("$.correlationId").value("method-123"))
                .andExpect(jsonPath("$.details").isEmpty());
        verifyNoInteractions(business);
        mvc.perform(get("/read-only")).andExpect(status().isOk());
        verify(business).run();
        verifyNoMoreInteractions(business);
    }

    @RestController
    static class ReadController {
        private final Runnable business;
        ReadController(Runnable business) { this.business = business; }
        @GetMapping("/read-only")
        void read() { business.run(); }
    }
}
