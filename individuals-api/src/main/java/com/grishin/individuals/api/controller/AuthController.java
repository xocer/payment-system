package com.grishin.individuals.api.controller;

import com.grishin.dto.TokenRefreshRequest;
import com.grishin.dto.TokenResponse;
import com.grishin.dto.UserInfoResponse;
import com.grishin.dto.UserLoginRequest;
import com.grishin.dto.UserRegistrationRequest;
import com.grishin.individuals.api.service.TokenService;
import com.grishin.individuals.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping("/register")
    public Mono<ResponseEntity<?>> register(@RequestBody UserRegistrationRequest request) {
        return userService.register(request)
                .map(tokenResponse -> ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody UserLoginRequest request) {
        return userService.login(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/refresh-token")
    public Mono<ResponseEntity<TokenResponse>> refresh(@RequestBody TokenRefreshRequest request) {
        return tokenService.refreshToken(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/me")
    public Mono<ResponseEntity<UserInfoResponse>> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        return userService.getUserInfo(jwt)
                .map(ResponseEntity::ok);
    }
}
