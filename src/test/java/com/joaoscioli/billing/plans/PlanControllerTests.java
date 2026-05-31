package com.joaoscioli.billing.plans;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
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
class PlanControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createPlanReturnsCreatedPlan() throws Exception {
        createOrganization("Acme Inc", "acme");

        mockMvc.perform(post("/api/organizations/acme/plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Starter",
                                  "code": "starter",
                                  "priceCents": 2900,
                                  "currency": "brl",
                                  "billingInterval": "MONTHLY"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.organizationSlug").value("acme"))
                .andExpect(jsonPath("$.name").value("Starter"))
                .andExpect(jsonPath("$.code").value("starter"))
                .andExpect(jsonPath("$.priceCents").value(2900))
                .andExpect(jsonPath("$.currency").value("BRL"))
                .andExpect(jsonPath("$.billingInterval").value("MONTHLY"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty());
    }

    @Test
    void listPlansReturnsOnlyPlansFromOrganization() throws Exception {
        createOrganization("Acme Inc", "acme");
        createOrganization("Beta Labs", "beta-labs");
        createPlan("acme", "Starter", "starter", 2900);
        createPlan("acme", "Growth", "growth", 9900);
        createPlan("beta-labs", "Starter", "starter", 3900);

        mockMvc.perform(get("/api/organizations/acme/plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].code", containsInAnyOrder("starter", "growth")));
    }

    @Test
    void findPlanByCodeReturnsPlan() throws Exception {
        createOrganization("Acme Inc", "acme");
        createPlan("acme", "Starter", "starter", 2900);

        mockMvc.perform(get("/api/organizations/acme/plans/starter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Starter"))
                .andExpect(jsonPath("$.code").value("starter"))
                .andExpect(jsonPath("$.priceCents").value(2900));
    }

    @Test
    void createPlanRejectsDuplicateCodeInsideOrganization() throws Exception {
        createOrganization("Acme Inc", "acme");
        createPlan("acme", "Starter", "starter", 2900);

        mockMvc.perform(post("/api/organizations/acme/plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Another Starter",
                                  "code": "starter",
                                  "priceCents": 3900,
                                  "currency": "BRL",
                                  "billingInterval": "MONTHLY"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Plan code already exists for organization: starter"));
    }

    @Test
    void createPlanAllowsSameCodeAcrossOrganizations() throws Exception {
        createOrganization("Acme Inc", "acme");
        createOrganization("Beta Labs", "beta-labs");
        createPlan("acme", "Starter", "starter", 2900);

        mockMvc.perform(post("/api/organizations/beta-labs/plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Starter",
                                  "code": "starter",
                                  "priceCents": 3900,
                                  "currency": "BRL",
                                  "billingInterval": "MONTHLY"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.organizationSlug").value("beta-labs"))
                .andExpect(jsonPath("$.code").value("starter"));
    }

    @Test
    void createPlanRejectsInvalidPrice() throws Exception {
        createOrganization("Acme Inc", "acme");

        mockMvc.perform(post("/api/organizations/acme/plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Free Trial",
                                  "code": "free-trial",
                                  "priceCents": 0,
                                  "currency": "BRL",
                                  "billingInterval": "MONTHLY"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void createPlanForMissingOrganizationReturnsNotFound() throws Exception {
        mockMvc.perform(post("/api/organizations/missing/plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Starter",
                                  "code": "starter",
                                  "priceCents": 2900,
                                  "currency": "BRL",
                                  "billingInterval": "MONTHLY"
                                }
                                """))
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

    private void createPlan(String organizationSlug, String name, String code, long priceCents) throws Exception {
        mockMvc.perform(post("/api/organizations/{organizationSlug}/plans", organizationSlug)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "%s",
                                  "code": "%s",
                                  "priceCents": %d,
                                  "currency": "BRL",
                                  "billingInterval": "MONTHLY"
                                }
                                """.formatted(name, code, priceCents)))
                .andExpect(status().isCreated());
    }
}
