package com.example.processoseletivoapi.responses;

import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String password,
        Set<RoleResponse> roles
) {
}
