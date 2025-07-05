package com.grishin.individuals.api.service;

import com.grishin.dto.TokenInfo;
import com.grishin.dto.TokenRefreshRequest;
import com.grishin.individuals.api.client.KeycloakClient;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final KeycloakClient keycloakClient;
    private final AtomicReference<TokenInfo> tokenInfo = new AtomicReference<>();

    public Mono<String> getClientToken() {
        TokenInfo cachedToken = tokenInfo.get();

        if (!isTokenExpired(cachedToken)) {
            var token = cachedToken.getToken();
            return Mono.just(token);
        }

        return keycloakClient.getClientToken()
                .flatMap(this::handleToken);
    }

    public Mono<ResponseEntity<?>> refreshToken(TokenRefreshRequest request) {
        return keycloakClient.refreshToken(request);
    }

    public List<String> getRoles(Claims claims) {
        Map<String, Object> resourceAccess = (Map<String, Object>) claims.get("resource_access", Map.class);
        if (resourceAccess == null) {
            return List.of();
        }

        Map<String, Object> account = (Map<String, Object>) resourceAccess.get("account");
        if (account == null) {
            return List.of();
        }

        List<String> roles = (List<String>) account.get("roles");
        return roles != null ? roles : List.of();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .build()
                .parseClaimsJwt(extractUnsignedJwt(token))
                .getBody();
    }

    private boolean isTokenExpired(TokenInfo token) {
        if (token == null || token.getExpirationTime() == null) {
            return true;
        }
        return token.getExpirationTime() <= Instant.now().getEpochSecond();
    }

    private String extractUnsignedJwt(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT format");
        }
        return parts[0] + "." + parts[1] + ".";
    }

    private Mono<String> handleToken(String token) {
        var claims = getClaims(token);

        long expiration = claims.getExpiration().toInstant().getEpochSecond();

        tokenInfo.set(new TokenInfo().token(token).expirationTime(expiration));
        return Mono.just(token);
    }
}
