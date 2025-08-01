package com.grishin.individuals.api.it;

import com.grishin.dto.TokenRefreshRequest;
import com.grishin.dto.TokenResponse;
import com.grishin.dto.UserLoginRequest;
import com.grishin.dto.UserRegistrationRequest;
import com.grishin.individuals.api.config.ContainersConfig;
import com.grishin.individuals.api.dto.UserInfoKeycloakResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.main.web-application-type=reactive")
@ImportTestcontainers(ContainersConfig.class)
@ActiveProfiles("test")
class AuthControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Test register functionality")
    void givenUserRegistrationRequest_whenRegister_thenSuccess() {
        //given
        var request = new UserRegistrationRequest()
                .username("ivanov@mail.com")
                .email("ivanov@mail.com")
                .firstName("Vasia")
                .lastName("Ivanov")
                .password("mypassword");

        //when
        var result = webTestClient.post()
                .uri("http://localhost:" + port + "/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange();

        //then
        result.expectStatus().isCreated();

        var tokenResponse = result.expectBody(TokenResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotBlank();

        webTestClient.get()
                .uri("http://localhost:" + port + "/v1/auth/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getAccessToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserInfoKeycloakResponse.class)
                .value(userInfo ->
                        assertThat(userInfo.email()).isEqualTo(request.getEmail()));
    }

    @Test
    @DisplayName("Test register duplicate functionality")
    void givenUserRegistrationRequest_whenRegisterSameUserTwice_thenError() {
        //given
        var request = new UserRegistrationRequest()
                .username("ivanov@mail.com")
                .email("ivanov@mail.com")
                .firstName("Vasia")
                .lastName("Ivanov")
                .password("mypassword");

        //when
        webTestClient.post()
                .uri("http://localhost:" + port + "/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange();
        var response = webTestClient.post()
                .uri("http://localhost:" + port + "/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange();

        //then
        response.expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .jsonPath("$.error").isEqualTo("Пользователь уже существует")
                .jsonPath("$.status").isEqualTo(409);
    }

    @Test
    @DisplayName("Test login functionality")
    void givenUserLoginRequest_whenLogin_thenSuccess() {
        //given
        var registrationRequest = new UserRegistrationRequest()
                .username("ivanova@mail.com")
                .email("ivanova@mail.com")
                .firstName("Maria")
                .lastName("Ivanova")
                .password("mypassword");

        webTestClient.post()
                .uri("http://localhost:" + port + "/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registrationRequest)
                .exchange();

        var userLoginRequest = new UserLoginRequest()
                .email(registrationRequest.getEmail())
                .password(registrationRequest.getPassword());

        // when
        var loginResponse = webTestClient.post()
                .uri("http://localhost:" + port + "/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userLoginRequest)
                .exchange();

        // then
        loginResponse
                .expectStatus().isOk()
                .expectBody(TokenResponse.class)
                .value(token -> {
                    var accessToken = token.getAccessToken();

                    assertThat(accessToken).isNotEmpty();
                    assertThat(token.getRefreshToken()).isNotEmpty();
                    assertThat(token.getExpiresIn()).isNotNull();
                    assertThat(token.getTokenType()).isNotNull();

                    webTestClient.get()
                            .uri("http://localhost:" + port + "/v1/auth/me")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody(UserInfoKeycloakResponse.class)
                            .value(userInfo ->
                                    assertThat(userInfo.email()).isEqualTo(registrationRequest.getEmail()));
                });
    }

    @Test
    @DisplayName("Test login with mistake in credentials functionality")
    void givenUserLoginRequest_whenLoginDataIncorrect_thenError() {
        //given
        var registrationRequest = new UserRegistrationRequest()
                .username("pushkin@mail.com")
                .email("pushkin@mail.com")
                .firstName("Petr")
                .lastName("Pushkin")
                .password("mypassword");

        webTestClient.post()
                .uri("http://localhost:" + port + "/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registrationRequest)
                .exchange();

        var userLoginRequest = new UserLoginRequest()
                .email(registrationRequest.getEmail())
                .password("fail password");

        // when
        var loginResponse = webTestClient.post()
                .uri("http://localhost:" + port + "/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userLoginRequest)
                .exchange();

        // then
        loginResponse.expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED)
                .expectBody()
                .jsonPath("$.error").isEqualTo("Неверный логин или пароль")
                .jsonPath("$.status").isEqualTo(401);
    }

    @Test
    @DisplayName("Test refresh token functionality")
    void givenRefreshToken_whenRefreshToken_thenGetNewToken() {
        // given
        var registrationRequest = new UserRegistrationRequest()
                .username("kuzmin@mail.com")
                .email("kuzmin@mail.com")
                .firstName("Alex")
                .lastName("Kuzmin")
                .password("mypassword");

        var result = webTestClient.post()
                .uri("http://localhost:" + port + "/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registrationRequest)
                .exchange();

        var tokenResponse = result.expectBody(TokenResponse.class)
                .returnResult()
                .getResponseBody();

        //when
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getRefreshToken()).isNotBlank();

        var refreshTokenResult = webTestClient.post()
                .uri("http://localhost:" + port + "/v1/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new TokenRefreshRequest().refreshToken(tokenResponse.getRefreshToken()))
                .exchange();

        //then
        result.expectStatus().isCreated();
        refreshTokenResult
                .expectStatus().isOk()
                .expectBody(TokenResponse.class)
                .value(token -> {
                    assertThat(token.getRefreshToken()).isNotBlank();
                    assertThat(token.getRefreshToken()).isNotEqualTo(tokenResponse.getRefreshToken());
                });
    }

    @Test
    @DisplayName("Test invalid refresh token functionality")
    void givenRefreshToken_whenRefreshTokenInvalid_thenError() {
        // given
        String invalidRefreshToken = "invalid";

        //when
        var refreshTokenResult = webTestClient.post()
                .uri("http://localhost:" + port + "/v1/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new TokenRefreshRequest().refreshToken(invalidRefreshToken))
                .exchange();

        //then
        refreshTokenResult.expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED)
                .expectBody()
                .jsonPath("$.error").isEqualTo("Недействительный или просроченный refresh token")
                .jsonPath("$.status").isEqualTo(401);
    }

    @Test
    @DisplayName("Test user info functionality")
    void givenToken_whenUserInfo_thenGetUserInfo() {
        //given
        var request = new UserRegistrationRequest()
                .username("bulochkina@mail.com")
                .email("bulochkina@mail.com")
                .firstName("Nadezda")
                .lastName("Bulochkina")
                .password("mypassword");

        //when
        var result = webTestClient.post()
                .uri("http://localhost:" + port + "/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange();

        //then
        result.expectStatus().isCreated();

        var tokenResponse = result.expectBody(TokenResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotBlank();

        webTestClient.get()
                .uri("http://localhost:" + port + "/v1/auth/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getAccessToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserInfoKeycloakResponse.class)
                .value(userInfo ->
                        assertThat(userInfo.email()).isEqualTo(request.getEmail()));
    }
}