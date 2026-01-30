package com.example.processoseletivoapi.responses;

import java.util.Set;

public record ArtistaResponse(
        Long id,
        String nome,
        Integer quantidadeDeAlbuns,
        Set<AlbumResponse> albuns
) {
}
