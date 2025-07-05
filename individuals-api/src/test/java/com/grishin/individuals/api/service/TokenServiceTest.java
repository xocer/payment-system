package com.grishin.individuals.api.service;

import com.grishin.dto.TokenRefreshRequest;
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
    public void givenToken_whenRefreshToken_thenKeycloakClientIsCalled() {
        TokenRefreshRequest request = new TokenRefreshRequest();
        BDDMockito.given(keycloakClient.refreshToken(request))
                .willReturn(Mono.just(ResponseEntity.ok("ok")));

        // when
        tokenService.refreshToken(request).block();

        // then
        verify(keycloakClient, times(1)).refreshToken(request);
    }
}