package com.joaoscioli.billing.common;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UnsupportedMediaTypeResponseTests {
    @Test
    void unsupportedContentTypePreservesAcceptHeaderAndStopsBeforeBusinessWork() throws Exception {
        var business = mock(Runnable.class);
        var mvc = MockMvcBuilders.standaloneSetup(new JsonController(business))
                .setControllerAdvice(new GlobalExceptionHandler()).build();

        mvc.perform(post("/json-only").contentType("text/plain").content("private payload")
                        .header("X-Correlation-Id", "media-123"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(header().string("Accept", containsString("application/json")))
                .andExpect(jsonPath("$.status").value(415))
                .andExpect(jsonPath("$.error").value("Unsupported Media Type"))
                .andExpect(jsonPath("$.message").value("Request content type is not supported"))
                .andExpect(jsonPath("$.path").value("/json-only"))
                .andExpect(jsonPath("$.correlationId").value("media-123"))
                .andExpect(jsonPath("$.details").isEmpty());
        verifyNoInteractions(business);

        mvc.perform(post("/json-only").contentType("application/json").content("{}"))
                .andExpect(status().isOk());
        verify(business).run();
        verifyNoMoreInteractions(business);
    }

    @RestController
    static class JsonController {
        private final Runnable business;
        JsonController(Runnable business) { this.business = business; }
        @PostMapping(value = "/json-only", consumes = "application/json")
        void create(@RequestBody java.util.Map<String, Object> body) { business.run(); }
    }
}
