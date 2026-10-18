package com.joaoscioli.billing.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UnreadableBodyResponseTests {
    @Test
    void validTypedJsonReachesBusinessWork() throws Exception {
        var businessWork = mock(Runnable.class);
        var mvc = MockMvcBuilders.standaloneSetup(new BodyController(businessWork))
                .setControllerAdvice(new GlobalExceptionHandler()).build();

        mvc.perform(post("/body-contract").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":\"00000000-0000-0000-0000-000000000001\","
                                + "\"startsOn\":\"2026-10-18\"}"))
                .andExpect(status().isOk());
        verify(businessWork).run();
        verifyNoMoreInteractions(businessWork);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "null", "{private-input", "{\"customerId\":\"private-input\"}",
            "{\"startsOn\":\"private-input\"}"})
    void invalidBodiesReturnTheApiErrorContractBeforeBusinessWork(String body) throws Exception {
        var businessWork = mock(Runnable.class);
        var mvc = MockMvcBuilders.standaloneSetup(new BodyController(businessWork))
                .setControllerAdvice(new GlobalExceptionHandler()).build();

        mvc.perform(post("/body-contract").contentType(MediaType.APPLICATION_JSON)
                        .header("X-Correlation-Id", "body-123").content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(
                        "Request body must contain valid JSON with the expected field types"))
                .andExpect(jsonPath("$.correlationId").value("body-123"))
                .andExpect(jsonPath("$.path").value("/body-contract"))
                .andExpect(jsonPath("$.details").isEmpty())
                .andExpect(content().string(not(containsString("private-input"))));
        verifyNoInteractions(businessWork);
    }

    record Body(UUID customerId, LocalDate startsOn) { }

    @RestController
    static class BodyController {
        private final Runnable businessWork;

        BodyController(Runnable businessWork) {
            this.businessWork = businessWork;
        }

        @PostMapping("/body-contract")
        void create(@RequestBody Body body) {
            businessWork.run();
        }
    }
}
