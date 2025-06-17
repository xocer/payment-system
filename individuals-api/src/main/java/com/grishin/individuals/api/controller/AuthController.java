//package com.grishin.individuals.api.controller;
//
//import com.grishin.dto.*;
//import com.grishin.individuals.api.client.KeycloakClient;
//import com.grishin.individuals.api.service.TokenService;
//import com.grishin.individuals.api.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequestMapping("/v1/auth")
//@RequiredArgsConstructor
//public class AuthController {
//    private final TokenService tokenService;
//    private final UserService userService;
//    private final KeycloakClient keycloakClient;
//
//    // Регистрация пользователя
//    @PostMapping("/registration")
//    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest request) {
//        Mono<Void> response = keycloakClient.registerUser(request);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
//
////    // Аутентификация пользователя
////    @PostMapping("/login")
////    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
////        TokenResponse response = authService.login(request);
////        return ResponseEntity.ok(response);
////    }
////
////    // Обновление токена доступа
////    @PostMapping("/refresh-token")
////    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) {
////        TokenResponse response = authService.refreshToken(request);
////        return ResponseEntity.ok(response);
////    }
////
////    // Получение данных текущего пользователя
////    @GetMapping("/me")
////    public ResponseEntity<?> getMe(@RequestHeader("Authorization") String authorizationHeader) {
////        UserInfoResponse response = authService.getCurrentUser(authorizationHeader);
////        return ResponseEntity.ok(response);
////    }
//}
