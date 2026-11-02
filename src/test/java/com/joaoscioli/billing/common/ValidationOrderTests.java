package com.joaoscioli.billing.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

class ValidationOrderTests {
    @Test
    void multipleErrorsHaveStableFieldOrderAndNeverExposeRejectedValues() throws Exception {
        var business = mock(Runnable.class);
        var mvc = MockMvcBuilders.standaloneSetup(new Controller(business))
                .setControllerAdvice(new GlobalExceptionHandler()).build();
        for (int attempt = 0; attempt < 3; attempt++) {
            mvc.perform(post("/validate").contentType("application/json")
                    .content("{\"zebra\":\"\",\"alpha\":\"\"}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.details[0].field").value("alpha"))
                    .andExpect(jsonPath("$.details[1].field").value("zebra"))
                    .andExpect(jsonPath("$.details.length()").value(2))
                    .andExpect(jsonPath("$.details[0].rejectedValue").doesNotExist());
        }
        verifyNoInteractions(business);
    }
    record Input(@NotBlank String zebra, @NotBlank String alpha) { }
    @RestController
    static class Controller {
        private final Runnable business;
        Controller(Runnable business) { this.business = business; }
        @PostMapping("/validate")
        void validate(@Valid @RequestBody Input input) { business.run(); }
    }
}
