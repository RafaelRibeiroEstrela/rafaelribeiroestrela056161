package com.example.processoseletivoapi.responses;

public record TokenResponse(
        String token,
        String refreshToken
) {
}
