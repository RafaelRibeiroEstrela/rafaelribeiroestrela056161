package com.example.processoseletivoapi.responses;

import java.util.Set;

public record ArtistaResponse(
        Long id,
        String nome,
        Set<AlbumResponse> albuns
) {
}
