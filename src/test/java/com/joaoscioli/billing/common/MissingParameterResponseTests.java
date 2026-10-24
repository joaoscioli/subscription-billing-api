package com.joaoscioli.billing.common;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MissingParameterResponseTests {
    @Test
    void missingRequiredParameterReturnsErrorEnvelopeWithoutBusinessWork() throws Exception {
        var business = mock(Runnable.class);
        var mvc = MockMvcBuilders.standaloneSetup(new SearchController(business))
                .setControllerAdvice(new GlobalExceptionHandler()).build();

        mvc.perform(get("/search").header("X-Correlation-Id", "search-123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Required request parameter is missing"))
                .andExpect(jsonPath("$.path").value("/search"))
                .andExpect(jsonPath("$.correlationId").value("search-123"))
                .andExpect(jsonPath("$.details").isEmpty());
        verifyNoInteractions(business);

        mvc.perform(get("/search").param("query", "active"))
                .andExpect(status().isOk());
        verify(business).run();
        verifyNoMoreInteractions(business);
    }

    @RestController
    static class SearchController {
        private final Runnable business;
        SearchController(Runnable business) { this.business = business; }

        @GetMapping("/search")
        void search(@RequestParam("query") String query) { business.run(); }
    }
}
