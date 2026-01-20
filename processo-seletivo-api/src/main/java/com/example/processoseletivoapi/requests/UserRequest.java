package com.example.processoseletivoapi.requests;

import java.util.Set;

public record UserRequest(
        String username,
        String password,
        Set<Long> rolesId
) {
}
