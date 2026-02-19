package com.orion.auth_service.dto;

import java.time.Instant;
import java.util.Set;

public record UserResponse(
        String username,
        String email,
        Boolean isVerified,
        Boolean isActive,
        String role,
        Set<String> permissions,
        Instant createdAt,
        Instant updatedAt
) {
}
