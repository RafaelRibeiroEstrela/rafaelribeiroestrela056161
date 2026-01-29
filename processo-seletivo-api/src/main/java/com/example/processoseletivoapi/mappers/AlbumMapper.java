package com.example.processoseletivoapi.mappers;

import com.example.processoseletivoapi.models.Album;
import com.example.processoseletivoapi.requests.AlbumRequest;
import com.example.processoseletivoapi.responses.AlbumResponse;
import org.springframework.stereotype.Component;

@Component
public class AlbumMapper {

    public Album requestToModel(AlbumRequest request) {
        if (request == null) return null;
        return new Album(null, request.nome());
    }

    public AlbumResponse modelToResponse(Album model) {
        if (model == null) return null;
        return new AlbumResponse(model.getId(), model.getNome());
    }
}
