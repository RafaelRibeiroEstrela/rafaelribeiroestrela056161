package com.example.processoseletivoapi.responses;

import java.util.List;

public record AlbumResponse(
        Long id,
        String nome,
        List<AlbumImagemResponse> capas
) {
}
