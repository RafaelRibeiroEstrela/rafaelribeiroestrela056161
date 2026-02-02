package com.example.processoseletivoapi.mappers;

import com.example.processoseletivoapi.models.Album;
import com.example.processoseletivoapi.models.AlbumImagem;
import com.example.processoseletivoapi.requests.AlbumRequest;
import com.example.processoseletivoapi.responses.AlbumImagemResponse;
import com.example.processoseletivoapi.responses.AlbumResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlbumMapper {

    private final AlbumImagemMapper albumImagemMapper;

    public AlbumMapper(AlbumImagemMapper albumImagemMapper) {
        this.albumImagemMapper = albumImagemMapper;
    }

    public Album requestToModel(AlbumRequest request) {
        if (request == null) return null;
        return new Album(null, request.nome());
    }

    public AlbumResponse modelToResponse(Album model) {
        if (model == null) return null;
        return new AlbumResponse(model.getId(), model.getNome(), null);
    }

    public AlbumResponse modelToResponse(Album model, List<AlbumImagem> imagens) {
        if (model == null) return null;
        List<AlbumImagemResponse> imagensResponse = imagens.stream().map(albumImagemMapper::modelToResponse).toList();
        return new AlbumResponse(model.getId(), model.getNome(), imagensResponse);
    }
}
