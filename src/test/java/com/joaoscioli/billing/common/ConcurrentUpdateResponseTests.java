package com.joaoscioli.billing.common;

import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ConcurrentUpdateResponseTests {
    @Test
    void concurrentUpdateReturnsConflictWithoutPersistenceDiagnostics() throws Exception {
        var mvc = MockMvcBuilders.standaloneSetup(new ConflictingController())
                .setControllerAdvice(new GlobalExceptionHandler()).build();

        mvc.perform(get("/conflict").header("X-Correlation-Id", "conflict-123"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Resource changed concurrently; reload before retrying"))
                .andExpect(jsonPath("$.correlationId").value("conflict-123"))
                .andExpect(jsonPath("$.path").value("/conflict"));
    }

    @RestController
    static class ConflictingController {
        @GetMapping("/conflict")
        String update() {
            throw new OptimisticLockingFailureException("private persistence diagnostics");
        }
    }
}
