package com.grishin.individuals.api.service;

import com.grishin.dto.UserInfoResponse;
import com.grishin.dto.UserLoginRequest;
import com.grishin.dto.UserRegistrationRequest;
import com.grishin.individuals.api.client.KeycloakClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserService {
    private KeycloakClient keycloakClient;
    private TokenService tokenService;

    public Mono<ResponseEntity<?>> register(UserRegistrationRequest request) {
        return tokenService.getClientToken()
                .flatMap(token -> keycloakClient.registerUser(request, token)
                        .thenReturn(ResponseEntity.ok().build()));
    }

    public Mono<ResponseEntity<?>> login(UserLoginRequest request) {
        return keycloakClient.login(request);
    }

    public Mono<ResponseEntity<?>> getUserInfo(String token) {
        return keycloakClient.getUserInfo(token)
                .flatMap(userInfo -> {
                    var claims = tokenService.getClaims(token.replace("Bearer ", "").trim());
                    var response = new UserInfoResponse()
                            .id(userInfo.sub())
                            .email(userInfo.email())
                            .roles(tokenService.getRoles(claims));
                    // где взять поле created_at
                    return Mono.just(ResponseEntity.ok(response));
                });
    }
}
