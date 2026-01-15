package com.example.processoseletivoapi.requests;

import java.util.Set;

public record AlbumRequest(
        String nome,
        Set<Long> artistaIdList
) {
}
