package com.grishin.individuals.api.service;

import com.grishin.dto.TokenResponse;
import com.grishin.dto.UserLoginRequest;
import com.grishin.dto.UserRegistrationRequest;
import com.grishin.individuals.api.client.KeycloakClient;
import com.grishin.individuals.api.dto.UserInfoKeycloakResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private KeycloakClient keycloakClient;
    @Mock
    private TokenService tokenService;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Test user registration")
    void givenUserRegistration_whenUserRegistration_thenRegisterUserIsCalledOnce() {
        //given
        var userRegistrationRequest = new UserRegistrationRequest()
                .email("test_mail@mail.com")
                .password("test_password");
        var mockedToken = "mocked-token";
        var mockTokenResponse = new TokenResponse().accessToken("access-token").refreshToken("refresh-token");
        BDDMockito.given(tokenService.getClientToken())
                .willReturn(Mono.just(mockedToken));
        BDDMockito.given(tokenService.getTokenResponse(any(String.class), any(String.class)))
                .willReturn(Mono.just(ResponseEntity.ok(mockTokenResponse)));
        BDDMockito.given(keycloakClient.registerUser(userRegistrationRequest, mockedToken))
                .willReturn(Mono.empty());

        //when
        ResponseEntity<?> response = userService.register(userRegistrationRequest).block();

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(keycloakClient, times(1)).registerUser(userRegistrationRequest, mockedToken);
    }

    @Test
    @DisplayName("Test login functionality")
    void givenUserLogin_whenLogin_thenLoginIsCalledOnce() {
        //given
        var userLoginRequest = new UserLoginRequest();
        BDDMockito.given(keycloakClient.login(userLoginRequest))
                .willReturn(Mono.just(ResponseEntity.ok(new TokenResponse())));

        //when
        var response = keycloakClient.login(userLoginRequest).block();

        //then
        assertThat(response).isNotNull();
        verify(keycloakClient, times(1)).login(userLoginRequest);
    }

    @Test
    @DisplayName("Test login functionality")
    void givenToken_whenGetUserInfo_thenGetUserInfoIsCalledOnce() {
        //given
        var mockedToken = "mocked-token";
        var userInfoResponse = new UserInfoKeycloakResponse(
                "sub-id",
                "test@email.com",
                List.of("mock"),
                LocalDateTime.now()
        );
        BDDMockito.given(keycloakClient.getUserInfo(mockedToken))
                .willReturn(Mono.just(userInfoResponse));

        //when
        var response = keycloakClient.getUserInfo(mockedToken).block();

        //then
        assertThat(response).isNotNull();
        verify(keycloakClient, times(1)).getUserInfo(mockedToken);
    }
}