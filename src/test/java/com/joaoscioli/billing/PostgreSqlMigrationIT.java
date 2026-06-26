package com.joaoscioli.billing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Testcontainers(disabledWithoutDocker = true)
class PostgreSqlMigrationIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void appliesFlywayMigrationsAgainstPostgres() {
        var successfulMigrations = jdbcTemplate.queryForObject(
                "select count(*) from flyway_schema_history where success = true",
                Integer.class
        );
        var organizationsTable = jdbcTemplate.queryForObject(
                "select to_regclass('public.organizations')",
                String.class
        );
        var subscriptionsTable = jdbcTemplate.queryForObject(
                "select to_regclass('public.subscriptions')",
                String.class
        );

        assertThat(successfulMigrations).isGreaterThanOrEqualTo(6);
        assertThat(organizationsTable).isEqualTo("organizations");
        assertThat(subscriptionsTable).isEqualTo("subscriptions");
    }
}
