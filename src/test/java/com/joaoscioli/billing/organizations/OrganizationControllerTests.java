package com.joaoscioli.billing.organizations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Sql(
        statements = {"DELETE FROM plans", "DELETE FROM customers", "DELETE FROM organizations"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class OrganizationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createOrganizationReturnsCreatedOrganization() throws Exception {
        mockMvc.perform(post("/api/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Acme Inc",
                                  "slug": "acme"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Acme Inc"))
                .andExpect(jsonPath("$.slug").value("acme"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty());
    }

    @Test
    void listOrganizationsReturnsCreatedOrganizations() throws Exception {
        createOrganization("Acme Inc", "acme");
        createOrganization("Beta Labs", "beta-labs");

        mockMvc.perform(get("/api/organizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].slug").value("acme"))
                .andExpect(jsonPath("$[1].slug").value("beta-labs"));
    }

    @Test
    void findOrganizationBySlugReturnsOrganization() throws Exception {
        createOrganization("Acme Inc", "acme");

        mockMvc.perform(get("/api/organizations/acme"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Acme Inc"))
                .andExpect(jsonPath("$.slug").value("acme"));
    }

    @Test
    void createOrganizationRejectsDuplicateSlug() throws Exception {
        createOrganization("Acme Inc", "acme");

        mockMvc.perform(post("/api/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Another Acme",
                                  "slug": "acme"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Organization slug already exists: acme"));
    }

    @Test
    void createOrganizationRejectsInvalidSlug() throws Exception {
        mockMvc.perform(post("/api/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Acme Inc",
                                  "slug": "Acme Inc"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void findOrganizationBySlugReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/organizations/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Organization not found: missing"));
    }

    private void createOrganization(String name, String slug) throws Exception {
        mockMvc.perform(post("/api/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "%s",
                                  "slug": "%s"
                                }
                                """.formatted(name, slug)))
                .andExpect(status().isCreated());
    }
}
