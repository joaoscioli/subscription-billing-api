package com.joaoscioli.billing.customers;

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
class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCustomerReturnsCreatedCustomer() throws Exception {
        createOrganization("Acme Inc", "acme");

        mockMvc.perform(post("/api/organizations/acme/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Ada Lovelace",
                                  "email": "ADA@ACME.COM"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.organizationSlug").value("acme"))
                .andExpect(jsonPath("$.name").value("Ada Lovelace"))
                .andExpect(jsonPath("$.email").value("ada@acme.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty());
    }

    @Test
    void listCustomersReturnsOnlyCustomersFromOrganization() throws Exception {
        createOrganization("Acme Inc", "acme");
        createOrganization("Beta Labs", "beta-labs");
        createCustomer("acme", "Ada Lovelace", "ada@acme.com");
        createCustomer("acme", "Grace Hopper", "grace@acme.com");
        createCustomer("beta-labs", "Alan Turing", "alan@beta-labs.com");

        mockMvc.perform(get("/api/organizations/acme/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].email", containsInAnyOrder("ada@acme.com", "grace@acme.com")));
    }

    @Test
    void findCustomerByIdReturnsCustomer() throws Exception {
        createOrganization("Acme Inc", "acme");
        var customerId = createCustomer("acme", "Ada Lovelace", "ada@acme.com");

        mockMvc.perform(get("/api/organizations/acme/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.name").value("Ada Lovelace"))
                .andExpect(jsonPath("$.email").value("ada@acme.com"));
    }

    @Test
    void createCustomerRejectsDuplicateEmailInsideOrganization() throws Exception {
        createOrganization("Acme Inc", "acme");
        createCustomer("acme", "Ada Lovelace", "ada@acme.com");

        mockMvc.perform(post("/api/organizations/acme/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Another Ada",
                                  "email": "ADA@ACME.COM"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Customer email already exists for organization: ada@acme.com"));
    }

    @Test
    void createCustomerAllowsSameEmailAcrossOrganizations() throws Exception {
        createOrganization("Acme Inc", "acme");
        createOrganization("Beta Labs", "beta-labs");
        createCustomer("acme", "Ada Lovelace", "ada@example.com");

        mockMvc.perform(post("/api/organizations/beta-labs/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Ada Lovelace",
                                  "email": "ada@example.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.organizationSlug").value("beta-labs"))
                .andExpect(jsonPath("$.email").value("ada@example.com"));
    }

    @Test
    void createCustomerForMissingOrganizationReturnsNotFound() throws Exception {
        mockMvc.perform(post("/api/organizations/missing/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Ada Lovelace",
                                  "email": "ada@acme.com"
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
}
