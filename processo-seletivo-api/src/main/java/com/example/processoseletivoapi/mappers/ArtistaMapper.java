package com.example.processoseletivoapi.mappers;

import com.example.processoseletivoapi.models.Album;
import com.example.processoseletivoapi.models.Artista;
import com.example.processoseletivoapi.requests.ArtistaRequest;
import com.example.processoseletivoapi.responses.AlbumResponse;
import com.example.processoseletivoapi.responses.ArtistaResponse;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ArtistaMapper {

    private final AlbumMapper albumMapper;

    public ArtistaMapper(AlbumMapper albumMapper) {
        this.albumMapper = albumMapper;
    }

    public Artista requestToModel(ArtistaRequest request) {
        if (request == null) return null;
        return new Artista(null, request.nome());
    }

    public ArtistaResponse modelToResponse(Artista model) {
        if (model == null) return null;
        return new ArtistaResponse(model.getId(), model.getNome(), null);
    }

    public ArtistaResponse modelToResponse(Artista model, Set<Album> albuns) {
        if (model == null) return null;
        Set<AlbumResponse> albunsResponse = albuns.stream().map(albumMapper::modelToResponse).collect(Collectors.toSet());
        return new ArtistaResponse(model.getId(), model.getNome(), albunsResponse);
    }
}
