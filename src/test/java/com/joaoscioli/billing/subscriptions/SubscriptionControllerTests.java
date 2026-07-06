package com.joaoscioli.billing.subscriptions;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        statements = {"DELETE FROM application_users", "DELETE FROM subscriptions", "DELETE FROM plans", "DELETE FROM customers", "DELETE FROM organizations"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class SubscriptionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createSubscriptionReturnsCreatedSubscription() throws Exception {
        createOrganization("Acme Inc", "acme");
        var customerId = createCustomer("acme", "Ada Lovelace", "ada@acme.com");
        createPlan("acme", "Starter", "starter", 2900, "MONTHLY");

        mockMvc.perform(post("/api/organizations/acme/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "planCode": "starter",
                                  "startsOn": "2026-06-01"
                                }
                                """.formatted(customerId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.organizationSlug").value("acme"))
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.customerName").value("Ada Lovelace"))
                .andExpect(jsonPath("$.planCode").value("starter"))
                .andExpect(jsonPath("$.billingInterval").value("MONTHLY"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.startsOn").value("2026-06-01"))
                .andExpect(jsonPath("$.currentPeriodStart").value("2026-06-01"))
                .andExpect(jsonPath("$.currentPeriodEnd").value("2026-07-01"))
                .andExpect(jsonPath("$.canceledAt").doesNotExist())
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty());
    }

    @Test
    void createYearlySubscriptionCalculatesAnnualPeriod() throws Exception {
        createOrganization("Acme Inc", "acme");
        var customerId = createCustomer("acme", "Ada Lovelace", "ada@acme.com");
        createPlan("acme", "Annual", "annual", 29900, "YEARLY");

        mockMvc.perform(post("/api/organizations/acme/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "planCode": "annual",
                                  "startsOn": "2026-06-01"
                                }
                                """.formatted(customerId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.billingInterval").value("YEARLY"))
                .andExpect(jsonPath("$.currentPeriodEnd").value("2027-06-01"));
    }

    @Test
    void listSubscriptionsReturnsOnlySubscriptionsFromOrganization() throws Exception {
        createOrganization("Acme Inc", "acme");
        createOrganization("Beta Labs", "beta-labs");
        var adaId = createCustomer("acme", "Ada Lovelace", "ada@acme.com");
        var graceId = createCustomer("acme", "Grace Hopper", "grace@acme.com");
        var alanId = createCustomer("beta-labs", "Alan Turing", "alan@beta-labs.com");
        createPlan("acme", "Starter", "starter", 2900, "MONTHLY");
        createPlan("acme", "Growth", "growth", 9900, "MONTHLY");
        createPlan("beta-labs", "Starter", "starter", 3900, "MONTHLY");
        createSubscription("acme", adaId, "starter");
        createSubscription("acme", graceId, "growth");
        createSubscription("beta-labs", alanId, "starter");

        mockMvc.perform(get("/api/organizations/acme/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].customerName", containsInAnyOrder("Ada Lovelace", "Grace Hopper")));
    }

    @Test
    void findSubscriptionByIdReturnsSubscription() throws Exception {
        createOrganization("Acme Inc", "acme");
        var customerId = createCustomer("acme", "Ada Lovelace", "ada@acme.com");
        createPlan("acme", "Starter", "starter", 2900, "MONTHLY");
        var subscriptionId = createSubscription("acme", customerId, "starter");

        mockMvc.perform(get("/api/organizations/acme/subscriptions/{id}", subscriptionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(subscriptionId))
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.planCode").value("starter"));
    }

    @Test
    void cancelSubscriptionReturnsCanceledSubscription() throws Exception {
        createOrganization("Acme Inc", "acme");
        var customerId = createCustomer("acme", "Ada Lovelace", "ada@acme.com");
        createPlan("acme", "Starter", "starter", 2900, "MONTHLY");
        var subscriptionId = createSubscription("acme", customerId, "starter");

        mockMvc.perform(post("/api/organizations/acme/subscriptions/{id}/cancel", subscriptionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(subscriptionId))
                .andExpect(jsonPath("$.status").value("CANCELED"))
                .andExpect(jsonPath("$.canceledAt").isNotEmpty());
    }

    @Test
    void renewMonthlySubscriptionAdvancesCurrentPeriod() throws Exception {
        createOrganization("Acme Inc", "acme");
        var customerId = createCustomer("acme", "Ada Lovelace", "ada@acme.com");
        createPlan("acme", "Starter", "starter", 2900, "MONTHLY");
        var subscriptionId = createSubscription("acme", customerId, "starter");

        mockMvc.perform(post("/api/organizations/acme/subscriptions/{id}/renew", subscriptionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(subscriptionId))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.currentPeriodStart").value("2026-07-01"))
                .andExpect(jsonPath("$.currentPeriodEnd").value("2026-08-01"));
    }

    @Test
    void renewYearlySubscriptionAdvancesCurrentPeriodByOneYear() throws Exception {
        createOrganization("Acme Inc", "acme");
        var customerId = createCustomer("acme", "Ada Lovelace", "ada@acme.com");
        createPlan("acme", "Annual", "annual", 29900, "YEARLY");
        var subscriptionId = createSubscription("acme", customerId, "annual");

        mockMvc.perform(post("/api/organizations/acme/subscriptions/{id}/renew", subscriptionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.billingInterval").value("YEARLY"))
                .andExpect(jsonPath("$.currentPeriodStart").value("2027-06-01"))
                .andExpect(jsonPath("$.currentPeriodEnd").value("2028-06-01"));
    }

    @Test
    void renewCanceledSubscriptionReturnsConflict() throws Exception {
        createOrganization("Acme Inc", "acme");
        var customerId = createCustomer("acme", "Ada Lovelace", "ada@acme.com");
        createPlan("acme", "Starter", "starter", 2900, "MONTHLY");
        var subscriptionId = createSubscription("acme", customerId, "starter");
        mockMvc.perform(post("/api/organizations/acme/subscriptions/{id}/cancel", subscriptionId))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/organizations/acme/subscriptions/{id}/renew", subscriptionId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Only active subscriptions can be renewed"));
    }

    @Test
    void canceledSubscriptionAllowsCustomerToSubscribeAgain() throws Exception {
        createOrganization("Acme Inc", "acme");
        var customerId = createCustomer("acme", "Ada Lovelace", "ada@acme.com");
        createPlan("acme", "Starter", "starter", 2900, "MONTHLY");
        createPlan("acme", "Growth", "growth", 9900, "MONTHLY");
        var subscriptionId = createSubscription("acme", customerId, "starter");

        mockMvc.perform(post("/api/organizations/acme/subscriptions/{id}/cancel", subscriptionId))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/organizations/acme/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "planCode": "growth",
                                  "startsOn": "2026-07-01"
                                }
                                """.formatted(customerId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.planCode").value("growth"));
    }

    @Test
    void createSubscriptionRejectsDuplicateActiveSubscriptionForCustomer() throws Exception {
        createOrganization("Acme Inc", "acme");
        var customerId = createCustomer("acme", "Ada Lovelace", "ada@acme.com");
        createPlan("acme", "Starter", "starter", 2900, "MONTHLY");
        createPlan("acme", "Growth", "growth", 9900, "MONTHLY");
        createSubscription("acme", customerId, "starter");

        mockMvc.perform(post("/api/organizations/acme/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "planCode": "growth",
                                  "startsOn": "2026-06-01"
                                }
                                """.formatted(customerId)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Customer already has an active subscription: " + customerId));
    }

    @Test
    void createSubscriptionForMissingPlanReturnsNotFound() throws Exception {
        createOrganization("Acme Inc", "acme");
        var customerId = createCustomer("acme", "Ada Lovelace", "ada@acme.com");

        mockMvc.perform(post("/api/organizations/acme/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "planCode": "missing",
                                  "startsOn": "2026-06-01"
                                }
                                """.formatted(customerId)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Plan not found: missing"));
    }

    @Test
    void createSubscriptionForMissingCustomerReturnsNotFound() throws Exception {
        createOrganization("Acme Inc", "acme");
        createPlan("acme", "Starter", "starter", 2900, "MONTHLY");
        var missingCustomerId = "11111111-1111-1111-1111-111111111111";

        mockMvc.perform(post("/api/organizations/acme/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "planCode": "starter",
                                  "startsOn": "2026-06-01"
                                }
                                """.formatted(missingCustomerId)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer not found: " + missingCustomerId));
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

    private String createCustomer(String organizationSlug, String name, String email) throws Exception {
        var result = mockMvc.perform(post("/api/organizations/{organizationSlug}/customers", organizationSlug)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "%s",
                                  "email": "%s"
                                }
                                """.formatted(name, email)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    private void createPlan(String organizationSlug, String name, String code, long priceCents, String interval) throws Exception {
        mockMvc.perform(post("/api/organizations/{organizationSlug}/plans", organizationSlug)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "%s",
                                  "code": "%s",
                                  "priceCents": %d,
                                  "currency": "BRL",
                                  "billingInterval": "%s"
                                }
                                """.formatted(name, code, priceCents, interval)))
                .andExpect(status().isCreated());
    }

    private String createSubscription(String organizationSlug, String customerId, String planCode) throws Exception {
        var result = mockMvc.perform(post("/api/organizations/{organizationSlug}/subscriptions", organizationSlug)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "planCode": "%s",
                                  "startsOn": "2026-06-01"
                                }
                                """.formatted(customerId, planCode)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }
}
