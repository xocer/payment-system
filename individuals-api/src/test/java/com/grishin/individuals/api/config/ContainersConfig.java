package com.grishin.individuals.api.config;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@TestConfiguration(proxyBeanMethods = false)
public class ContainersConfig {

    private static final String KC_PG_USER_PASS = "keycloak";
    private static final String KC_PG_DB = "keycloak_db";
    private static final String POSTGRES_IMAGE = "postgres:17";
    private static final String KEYCLOAK_IMAGE = "quay.io/keycloak/keycloak:26.2";
    private static final String REALM_IMPORT_FILE = "keycloak/realm-config.json";

    private static final Network NETWORK = Network.newNetwork();

    @Container
    public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(POSTGRES_IMAGE)
            .withDatabaseName(KC_PG_DB)
            .withUsername(KC_PG_USER_PASS)
            .withPassword(KC_PG_USER_PASS)
            .withNetwork(NETWORK)
            .withNetworkAliases("postgres");

    @Container
    public static final KeycloakContainer KEYCLOAK = new KeycloakContainer(KEYCLOAK_IMAGE)
            .withEnv("KEYCLOAK_ADMIN", KC_PG_USER_PASS)
            .withEnv("KEYCLOAK_ADMIN_PASSWORD", KC_PG_USER_PASS)
            .withEnv("KC_DB", "postgres")
            .withEnv("KC_DB_USERNAME", POSTGRES.getUsername())
            .withEnv("KC_DB_PASSWORD", POSTGRES.getPassword())
            .withEnv("KC_DB_URL_HOST", "postgres")
            .withEnv("KC_DB_URL_DATABASE", POSTGRES.getDatabaseName())
            .withNetwork(NETWORK)
            .dependsOn(POSTGRES)
            .withRealmImportFile(REALM_IMPORT_FILE);

    @DynamicPropertySource
    static void registry(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("keycloak.base-url", KEYCLOAK::getAuthServerUrl);
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> KEYCLOAK.getAuthServerUrl() + "/realms/payment-system");
    }
}