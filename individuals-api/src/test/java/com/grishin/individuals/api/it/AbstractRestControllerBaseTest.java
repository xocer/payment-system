package com.grishin.individuals.api.it;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class AbstractRestControllerBaseTest {

    @Container
    private static final KeycloakContainer CONTAINER;

    static {
        CONTAINER = new KeycloakContainer("quay.io/keycloak/keycloak:26.2")
                .withRealmImportFile("keycloak/realm-config.json");
        CONTAINER.start();
    }

    public static String getAuthServerUrl() {
        return CONTAINER.getAuthServerUrl();
    }

//    @DynamicPropertySource
//    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
//        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
//        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
//    }
}
