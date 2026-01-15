package com.example.processoseletivoapi.mappers;

import com.example.processoseletivoapi.models.Artista;
import com.example.processoseletivoapi.requests.ArtistaRequest;
import com.example.processoseletivoapi.responses.ArtistaResponse;
import org.springframework.stereotype.Component;

@Component
public class ArtistaMapper {

    public Artista requestToModel(ArtistaRequest request) {
        if (request == null) return null;
        return new Artista(null, request.nome());
    }

    public ArtistaResponse modelToResponse(Artista model) {
        if (model == null) return null;
        return new ArtistaResponse(model.getId(), model.getNome());
    }
}
