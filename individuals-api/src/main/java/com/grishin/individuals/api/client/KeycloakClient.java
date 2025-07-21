package com.grishin.individuals.api.client;

import com.grishin.dto.TokenRefreshRequest;
import com.grishin.dto.TokenResponse;
import com.grishin.dto.UserLoginRequest;
import com.grishin.dto.UserRegistrationRequest;
import com.grishin.individuals.api.common.KeycloakRequestParam;
import com.grishin.individuals.api.config.KeycloakProperties;
import com.grishin.individuals.api.dto.UserInfoKeycloakResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KeycloakClient {
    private final KeycloakProperties props;
    private final WebClient webClient = WebClient.create();

    public Mono<Void> registerUser(UserRegistrationRequest request, String token) {
        Map<String, Object> body = Map.of(
                KeycloakRequestParam.USERNAME.getKey(), request.getUsername(),
                KeycloakRequestParam.EMAIL.getKey(), request.getEmail(),
                KeycloakRequestParam.FIRST_NAME.getKey(), request.getFirstName(),
                KeycloakRequestParam.LAST_NAME.getKey(), request.getLastName(),
                KeycloakRequestParam.IS_ACCOUNT_ENABLED.getKey(), true,
                KeycloakRequestParam.IS_EMAIL_VERIFIED.getKey(), true,
                KeycloakRequestParam.CREDENTIALS.getKey(), List.of(
                        Map.of(
                                KeycloakRequestParam.CREDENTIAL_TYPE.getKey(), props.getCredentialType().getPassword(),
                                KeycloakRequestParam.CREDENTIALS_VALUE.getKey(), request.getPassword(),
                                KeycloakRequestParam.IS_CREDENTIAL_TEMPORARY.getKey(), false
                        )
                )
        );
        return webClient.post()
                .uri(getAdminUsersEndpoint())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<String> getClientToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(KeycloakRequestParam.CLIENT_ID.getKey(), props.getClientId());
        formData.add(KeycloakRequestParam.CLIENT_SECRET.getKey(), props.getClientSecret());
        formData.add(KeycloakRequestParam.GRANT_TYPE.getKey(), props.getCredentialType().getClientCredentials());

        return webClient.post()
                .uri(getTokenEndpoint())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .mapNotNull(TokenResponse::getAccessToken);
    }

    public Mono<ResponseEntity<?>> login(UserLoginRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(KeycloakRequestParam.CLIENT_ID.getKey(), props.getClientId());
        formData.add(KeycloakRequestParam.CLIENT_SECRET.getKey(), props.getClientSecret());
        formData.add(KeycloakRequestParam.USERNAME.getKey(), request.getEmail());
        formData.add(KeycloakRequestParam.PASSWORD.getKey(), request.getPassword());
        formData.add(KeycloakRequestParam.GRANT_TYPE.getKey(), props.getCredentialType().getPassword());

        return webClient.post()
                .uri(getTokenEndpoint())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<?>> refreshToken(TokenRefreshRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(KeycloakRequestParam.CLIENT_ID.getKey(), props.getClientId());
        formData.add(KeycloakRequestParam.CLIENT_SECRET.getKey(), props.getClientSecret());
        formData.add(KeycloakRequestParam.REFRESH_TOKEN.getKey(), request.getRefreshToken());
        formData.add(KeycloakRequestParam.GRANT_TYPE.getKey(), props.getCredentialType().getRefreshToken());

        return webClient.post()
                .uri(getTokenEndpoint())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .map(ResponseEntity::ok);
    }

    public Mono<UserInfoKeycloakResponse> getUserInfo(String token) {
        return webClient.get()
                .uri(getUserInfoEndpoint())
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(UserInfoKeycloakResponse.class);
    }

    private String getTokenEndpoint() {
        return String.format("%s/realms/%s/protocol/openid-connect/token", props.getBaseUrl(), props.getRealm());
    }

    private String getUserInfoEndpoint() {
        return String.format("%s/realms/%s/protocol/openid-connect/userinfo", props.getBaseUrl(), props.getRealm());
    }

    private String getAdminUsersEndpoint() {
        return String.format("%s/admin/realms/%s/users", props.getBaseUrl(), props.getRealm());
    }
}
