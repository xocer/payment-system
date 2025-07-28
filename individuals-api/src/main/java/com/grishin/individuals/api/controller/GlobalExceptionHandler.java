package com.grishin.individuals.api.controller;

import com.grishin.dto.ErrorResponse;
import com.grishin.individuals.api.exception.InvalidCredentialsException;
import com.grishin.individuals.api.exception.InvalidRefreshTokenException;
import com.grishin.individuals.api.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse().error(ex.getMessage()).status(HttpStatus.CONFLICT.value());
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInvalidCredentials(InvalidCredentialsException ex) {
        ErrorResponse error = new ErrorResponse().error(ex.getMessage()).status(HttpStatus.UNAUTHORIZED.value());
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error));
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInvalidRefreshToken(InvalidRefreshTokenException ex) {
        ErrorResponse error = new ErrorResponse().error(ex.getMessage()).status(HttpStatus.UNAUTHORIZED.value());
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInvalidGetUserInfo(UsernameNotFoundException ex) {
        ErrorResponse error = new ErrorResponse().error(ex.getMessage()).status(HttpStatus.NOT_FOUND.value());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }
}
