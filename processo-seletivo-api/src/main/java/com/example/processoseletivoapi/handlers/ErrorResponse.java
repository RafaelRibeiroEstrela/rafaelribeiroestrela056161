package com.example.processoseletivoapi.handlers;

import java.time.LocalDateTime;

public record ErrorResponse(
        int code,
        String error,
        String describle,
        LocalDateTime instant,
        String path
) {
}
