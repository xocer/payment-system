package com.grishin.individuals.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UserInfoKeycloakResponse(String sub, String email, List<String> roles, LocalDateTime createdAt) {
}
