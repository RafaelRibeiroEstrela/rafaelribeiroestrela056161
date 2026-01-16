package com.example.processoseletivoapi.mappers;

import com.example.processoseletivoapi.models.Regional;
import com.example.processoseletivoapi.responses.RegionalResponse;
import org.springframework.stereotype.Component;

@Component
public class RegionalMapper {

    public Regional responseToModel(RegionalResponse response) {
        if (response == null) return null;
        return new Regional(null, response.id(), response.nome(), true);
    }

    public RegionalResponse modelToResponse(Regional model) {
        if (model == null) return null;
        return new RegionalResponse(model.getId(), model.getNome(), model.isAtivo());
    }
}
