package com.grishin.individuals.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private String baseUrl;
    private String realm;
    private String clientId;
    private String clientSecret;
    private CredentialType credentialType = new CredentialType();

    @Getter
    @Setter
    public static class CredentialType {
        private String password;
        private String clientCredentials;
        private String refreshToken;
    }
}
