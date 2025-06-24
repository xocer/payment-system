package com.grishin.individuals.api.client;

import com.grishin.dto.TokenRefreshRequest;
import com.grishin.dto.TokenResponse;
import com.grishin.dto.UserLoginRequest;
import com.grishin.dto.UserRegistrationRequest;
import com.grishin.individuals.api.common.KeycloakRequestParam;
import com.grishin.individuals.api.dto.UserInfoKeycloakResponse;
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
public class KeycloakClient {
    WebClient webClient = WebClient.create();

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
                                KeycloakRequestParam.CREDENTIAL_TYPE.getKey(), "password",
                                KeycloakRequestParam.CREDENTIALS_VALUE.getKey(), request.getPassword(),
                                KeycloakRequestParam.IS_CREDENTIAL_TEMPORARY.getKey(), false
                        )
                )
        );
        return webClient.post()
                .uri("http://localhost:9090/admin/realms/payment-system/users")
                .header("Authorization", "Bearer " + token)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<String> getClientToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(KeycloakRequestParam.CLIENT_ID.getKey(), "individuals-api");
        formData.add(KeycloakRequestParam.CLIENT_SECRET.getKey(), "dLBKPjDAyC5Zmg4N8EgStfQhWFYZQQi7");
        formData.add(KeycloakRequestParam.GRANT_TYPE.getKey(), "client_credentials");

        return webClient.post()
                .uri("http://localhost:9090/realms/payment-system/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .mapNotNull(TokenResponse::getAccessToken);
    }

    public Mono<ResponseEntity<?>> login(UserLoginRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(KeycloakRequestParam.CLIENT_ID.getKey(), "individuals-api");
        formData.add(KeycloakRequestParam.CLIENT_SECRET.getKey(), "dLBKPjDAyC5Zmg4N8EgStfQhWFYZQQi7");
        formData.add(KeycloakRequestParam.USERNAME.getKey(), request.getEmail());
        formData.add(KeycloakRequestParam.PASSWORD.getKey(), request.getPassword());
        formData.add(KeycloakRequestParam.GRANT_TYPE.getKey(), "password");

        return webClient.post()
                .uri("http://localhost:9090/realms/payment-system/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<?>> refreshToken(TokenRefreshRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(KeycloakRequestParam.CLIENT_ID.getKey(), "individuals-api");
        formData.add(KeycloakRequestParam.CLIENT_SECRET.getKey(), "dLBKPjDAyC5Zmg4N8EgStfQhWFYZQQi7");
        formData.add(KeycloakRequestParam.REFRESH_TOKEN.getKey(), request.getRefreshToken());
        formData.add(KeycloakRequestParam.GRANT_TYPE.getKey(), "refresh_token");

        return webClient.post()
                .uri("http://localhost:9090/realms/payment-system/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .map(ResponseEntity::ok);
    }

    public Mono<UserInfoKeycloakResponse> getUserInfo(String token) {
        return webClient.get()
                .uri("http://localhost:9090/realms/payment-system/protocol/openid-connect/userinfo")
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(UserInfoKeycloakResponse.class);
    }
}
