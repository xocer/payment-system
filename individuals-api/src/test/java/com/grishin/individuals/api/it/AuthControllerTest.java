//package com.grishin.individuals.api.it;
//
//import com.grishin.dto.TokenResponse;
//import com.grishin.dto.UserLoginRequest;
//import com.grishin.dto.UserRegistrationRequest;
//import com.grishin.individuals.api.config.ContainersConfig;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.boot.testcontainers.context.ImportTestcontainers;
//import org.springframework.core.env.Environment;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.reactive.server.WebTestClient;
//
//import java.util.Arrays;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.main.web-application-type=reactive")
//@ImportTestcontainers(ContainersConfig.class)
//@ActiveProfiles("test")
//class AuthControllerTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @Test
//    @DisplayName("Test register functionality")
//    void givenUserRegistrationRequest_whenRegister_thenSuccess() {
//        //given
//        var request = new UserRegistrationRequest()
//                .username("ivanov@mail.com")
//                .email("ivanov@mail.com")
//                .firstName("Vasia")
//                .lastName("Ivanov")
//                .password("mypassword");
//
//        //when
//        var result = webTestClient.post()
//                .uri("http://localhost:" + port + "/v1/auth/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(request)
//                .exchange();
//
//        //then
//        result.expectBody().isEmpty();
//        result.expectStatus().isOk();
//    }
//
//    @Test
//    @DisplayName("Test login functionality")
//    void givenUserLoginRequest_whenLogin_thenSuccess() {
//        //given
//        var registrationRequest = new UserRegistrationRequest()
//                .username("ivanov@mail.com")
//                .email("ivanov@mail.com")
//                .firstName("Vasia")
//                .lastName("Ivanov")
//                .password("mypassword");
//
//        webTestClient.post()
//                .uri("http://localhost:" + port + "/v1/auth/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(registrationRequest)
//                .exchange();
//
//        var userLoginRequest = new UserLoginRequest()
//                .email(registrationRequest.getEmail())
//                .password(registrationRequest.getPassword());
//
//        // when
//        var loginResponse = webTestClient.post()
//                .uri("http://localhost:" + port + "/v1/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(userLoginRequest)
//                .exchange();
//
//        // then
//        loginResponse.expectStatus().isOk();
//        loginResponse
//                .expectBody(TokenResponse.class)
//                .value(token -> {
//                    assertThat(token.getAccessToken()).isNotEmpty();
//                    assertThat(token.getRefreshToken()).isNotEmpty();
//                    assertThat(token.getExpiresIn()).isNotNull();
//                    assertThat(token.getTokenType()).isNotNull();
//                });
//    }
//
//    @Test
//    @DisplayName("Test refresh token functionality")
//    void givenRefreshToken_whenRefreshToken_thenGetNewToken() {
//
//    }
//
//    @Test
//    @DisplayName("Test user info functionality")
//    void givenToken_whenUserInfo_thenGetUserInfo() {
//
//    }
//}