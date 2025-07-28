package com.grishin.individuals.api.dto;

import java.util.Map;

public record UserInfoKeycloakResponse(String id, String email, Map<String, Boolean> access, Long createdTimestamp) {
}
