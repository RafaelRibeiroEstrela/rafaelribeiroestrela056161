package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.mappers.RegionalMapper;
import com.example.processoseletivoapi.models.Regional;
import com.example.processoseletivoapi.responses.RegionalResponse;
import com.example.processoseletivoapi.services.RegionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "REGIONAL")
@RestController
@RequestMapping("/v1/regionais")
public class RegionalController {

    private final RegionalService service;
    private final RegionalMapper mapper;

    public RegionalController(RegionalService service, RegionalMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Sincronizar base de regionais",
            description = "Executa a sincronização das regionais com a fonte externa e atualiza a base local."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sincronização executada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno ao executar sincronização"
            )
    })
    @GetMapping("/synchronize")
    public ResponseEntity<String> sync() {
        service.sync();
        return ResponseEntity.ok().body("Base de dados sincronizada");
    }

    @Operation(
            summary = "Listar todas as regionais",
            description = "Retorna a lista completa de regionais cadastradas na base local."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de regionais retornada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno ao consultar regionais",
                    content = @Content
            )
    })
    @GetMapping("/all")
    public ResponseEntity<List<RegionalResponse>> findAll() {
        List<Regional> models = service.findAll();
        return ResponseEntity.ok().body(models.stream().map(mapper::modelToResponse).toList());
    }
}
