package com.grishin.individuals.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum KeycloakRequestParam {
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    GRANT_TYPE("grant_type"),
    REFRESH_TOKEN("refresh_token"),
    USERNAME("username"),
    PASSWORD("password"),
    EMAIL("email"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    IS_ACCOUNT_ENABLED("enabled"),
    IS_EMAIL_VERIFIED("emailVerified"),
    CREDENTIALS("credentials"),
    CREDENTIAL_TYPE("type"),
    CREDENTIALS_VALUE("value"),
    IS_CREDENTIAL_TEMPORARY("temporary");

    private final String key;
}
