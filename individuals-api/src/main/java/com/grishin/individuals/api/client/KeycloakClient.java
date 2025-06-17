//package com.grishin.individuals.api.client;
//
//import com.grishin.dto.UserRegistrationRequest;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//@Component
//public class KeycloakClient {
//    WebClient webClient = WebClient.create("http://localhost:9090/admin/realms/payment-system/users");
////    @Value("${keycloak.admin.token}")
////    private String adminToken;
////
////    @Value("${keycloak.url}")
////    private String keycloakUrl;
////
////    @Value("${keycloak.realm}")
////    private String realm;
//
//    public Mono<Void> registerUser(UserRegistrationRequest request) {
//        var authorization = webClient.post()
//                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJUeW9meDl4Y21DZzZuam93elFUZkd1b0FPNjJJWUQ1b19MTmRVWG9ySWI4In0.eyJleHAiOjE3NTAwOTExOTQsImlhdCI6MTc1MDA5MDg5NCwianRpIjoidHJydGNjOjNkMzNmYTM0LTJhMDgtNGUwNy1iMzFmLTQzNzg1ZTc1MWEzYSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6OTA5MC9yZWFsbXMvcGF5bWVudC1zeXN0ZW0iLCJhdWQiOlsicmVhbG0tbWFuYWdlbWVudCIsImFjY291bnQiXSwic3ViIjoiZTJmNDMwODEtNmRkMy00YmRjLTlkNmMtZGE0NTFiZDhlYzFjIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiaW5kaXZpZHVhbHMtYXBpIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjgwOTEiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtcGF5bWVudC1zeXN0ZW0iLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiaW5kaXZpZHVhbHMtYXBpIjp7InJvbGVzIjpbInVtYV9wcm90ZWN0aW9uIl19LCJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbIm1hbmFnZS11c2VycyIsInZpZXctdXNlcnMiLCJxdWVyeS1ncm91cHMiLCJxdWVyeS11c2VycyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiY2xpZW50SG9zdCI6IjE5Mi4xNjguNjUuMSIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC1pbmRpdmlkdWFscy1hcGkiLCJjbGllbnRBZGRyZXNzIjoiMTkyLjE2OC42NS4xIiwiY2xpZW50X2lkIjoiaW5kaXZpZHVhbHMtYXBpIn0.Td5Ve3Cx12UCtbNv3XfUJXhqWfhYFVHYfCWJDCjLedmbpm4yvpxk_J180nSW7eYkEvaH8OyNmDEmKgbWBW2spFIAUWehB0-NkY9ZovHEbs9Xxv1ulIBK7GOR4Mq2C_J3uK_3CuCx2yoO2N9ebfiWni9m-rET85H1FQSAvFr9s9yl8tZ_HeXRG1JJIJadr8U36M_MgxQPOS2ymdCe8E2jn5AIMrgNFWeHBh7wsGXhdFx0mnSdKrX6kx5puCvUa9oeWl1dtzWlkeHpao6dorhp5Hxhnod9WEFrrxu8ryYem1z1CddiDnUahCh-_grWIIbklgrG9f1T1CLJdl-g6fokjQ")
//                .bodyValue(request)
//                .retrieve()
//                .bodyToMono(Void.class);
//        return authorization;
//    }
//
//}
