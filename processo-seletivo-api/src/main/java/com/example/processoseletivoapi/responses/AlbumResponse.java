package com.example.processoseletivoapi.responses;

import java.util.Set;

public record AlbumResponse(
        Long id,
        String nome,
        Set<ArtistaResponse> artistas
) {
}
