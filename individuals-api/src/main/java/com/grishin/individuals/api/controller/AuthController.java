package com.grishin.individuals.api.controller;

import com.grishin.dto.*;
import com.grishin.individuals.api.service.TokenService;
import com.grishin.individuals.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping("/register")
    public Mono<ResponseEntity<?>>  register(@RequestBody UserRegistrationRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody UserLoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/refresh-token")
    public Mono<ResponseEntity<?>> refresh(@RequestBody TokenRefreshRequest request) {
        return tokenService.refreshToken(request);
    }

    @GetMapping("/me")
    public Mono<ResponseEntity<?>> getUserInfo(@RequestHeader("Authorization") String token) {
        return userService.getUserInfo(token);
    }
}
