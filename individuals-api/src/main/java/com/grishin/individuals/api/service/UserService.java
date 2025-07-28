package com.grishin.individuals.api.service;

import com.grishin.dto.TokenResponse;
import com.grishin.dto.UserInfoResponse;
import com.grishin.dto.UserLoginRequest;
import com.grishin.dto.UserRegistrationRequest;
import com.grishin.individuals.api.client.KeycloakClient;
import com.grishin.individuals.api.exception.InvalidCredentialsException;
import com.grishin.individuals.api.exception.UserAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {
    private KeycloakClient keycloakClient;
    private TokenService tokenService;

    public Mono<TokenResponse> register(UserRegistrationRequest request) {
        return tokenService.getClientToken()
                .flatMap(token -> keycloakClient.registerUser(request, token))
                .onErrorMap(_ -> new UserAlreadyExistsException("Пользователь уже существует"))
                .then(Mono.defer(() -> tokenService.getTokenResponse(request.getEmail(), request.getPassword())));
    }

    public Mono<TokenResponse> login(UserLoginRequest request) {
        return keycloakClient.login(request)
                .onErrorMap(_ -> new InvalidCredentialsException("Неверный логин или пароль"));
    }

    public Mono<UserInfoResponse> getUserInfo(Jwt jwt) {
        var userId = (String) jwt.getClaim("sub");
        return tokenService.getClientToken()
                .flatMap(clientToken -> keycloakClient.getUserInfo(clientToken, userId))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new UsernameNotFoundException("Учетная запись удалена"));
                    } else if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                        return Mono.error(new InvalidCredentialsException("Токен невалиден"));
                    }
                    return Mono.error(ex);
                })
                .flatMap(userInfo -> {
                    var response = new UserInfoResponse()
                            .id(userInfo.id())
                            .email(userInfo.email())
                            .roles(userInfo.access().entrySet().stream()
                                    .filter(Map.Entry::getValue)
                                    .map(Map.Entry::getKey)
                                    .toList())
                            .createdAt(Instant.ofEpochMilli(userInfo.createdTimestamp())
                                    .atZone(ZoneId.of("Europe/Moscow"))
                                    .toOffsetDateTime());
                    return Mono.just(response);
                });
    }
}
