package com.grishin.individuals.api.controller;

import com.grishin.dto.*;
import com.grishin.individuals.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // Регистрация пользователя
    @PostMapping("/registration")
    public ResponseEntity<TokenResponse> register(@RequestBody UserRegistrationRequest request) {
        TokenResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Аутентификация пользователя
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserLoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // Обновление токена доступа
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refresh(@RequestBody TokenRefreshRequest request) {
        TokenResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    // Получение данных текущего пользователя
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMe(@RequestHeader("Authorization") String authorizationHeader) {
        UserInfoResponse response = authService.getCurrentUser(authorizationHeader);
        return ResponseEntity.ok(response);
    }
}
