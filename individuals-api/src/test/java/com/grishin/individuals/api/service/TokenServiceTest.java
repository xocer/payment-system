package com.grishin.individuals.api.service;

import com.grishin.dto.TokenRefreshRequest;
import com.grishin.dto.TokenResponse;
import com.grishin.individuals.api.client.KeycloakClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    @Mock
    private KeycloakClient keycloakClient;
    @InjectMocks
    private TokenService tokenService;

    @Test
    @DisplayName("Test refresh token functionality")
    void givenToken_whenRefreshToken_thenKeycloakClientIsCalled() {
        //given
        var request = new TokenRefreshRequest();
        BDDMockito.given(keycloakClient.refreshToken(any(TokenRefreshRequest.class)))
                .willReturn(Mono.just(ResponseEntity.ok(new TokenResponse())));

        // when
        tokenService.refreshToken(new TokenRefreshRequest()).block();

        // then
        verify(keycloakClient, times(1)).refreshToken(request);
    }

    @Test
    @DisplayName("Should return cached client token on second call")
    void givenToken_whenGetClientTokenTwice_thenKeycloakClientIsCalledOnce() {
        //given
        var token = generateValidJwt();
        BDDMockito.given(keycloakClient.getClientToken())
                .willReturn(Mono.just(token));

        // when
        var newToken = tokenService.getClientToken().block();
        var cachedToken = tokenService.getClientToken().block();

        // then
        assertThat(token).isEqualTo(newToken);
        assertThat(token).isEqualTo(cachedToken);
        verify(keycloakClient, times(1)).getClientToken();
    }

    String generateValidJwt() {
        var header = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        var exp = Instant.now().plusSeconds(3600).getEpochSecond();
        var payload = String.format("{\"exp\":%d}", exp);
        var payloadBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
        var signature = "ignoredSignature";

        return header + "." + payloadBase64 + "." + signature;
    }
}