package com.example.processoseletivoapi.requests;

import java.util.Set;

public record ArtistaRequest(
        String nome,
        Set<Long> listaAlbumId
) {
}
