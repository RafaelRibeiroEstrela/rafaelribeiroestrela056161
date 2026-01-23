package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.mappers.ArtistaMapper;
import com.example.processoseletivoapi.models.Artista;
import com.example.processoseletivoapi.requests.ArtistaRequest;
import com.example.processoseletivoapi.responses.ArtistaResponse;
import com.example.processoseletivoapi.services.ArtistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ARTISTA")
@RestController
@RequestMapping("/v1/artistas")
public class ArtistaController {

    private final ArtistaService service;
    private final ArtistaMapper mapper;

    public ArtistaController(ArtistaService service, ArtistaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Criar artista",
            description = "Cria um novo artista."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Artista criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ArtistaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content)
    })
    @Transactional
    @PostMapping
    public ResponseEntity<ArtistaResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados para criação do artista",
                    content = @Content(schema = @Schema(implementation = ArtistaRequest.class))
            )
            @RequestBody ArtistaRequest request
    ) {
        Artista model = service.create(mapper.requestToModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.modelToResponse(model));
    }

    @Operation(
            summary = "Atualizar artista",
            description = "Atualiza um artista existente pelo ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ArtistaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado", content = @Content)
    })
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<ArtistaResponse> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados para atualização do artista",
                    content = @Content(schema = @Schema(implementation = ArtistaRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody ArtistaRequest request,
            @Parameter(description = "ID do artista", example = "1", required = true)
            @PathVariable Long id
    ) {
        Artista model = service.update(mapper.requestToModel(request), id);
        return ResponseEntity.ok(mapper.modelToResponse(model));
    }

    @Operation(
            summary = "Excluir artista",
            description = "Remove um artista pelo ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Artista excluído com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado", content = @Content)
    })
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do artista", example = "1", required = true)
            @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar artistas",
            description = "Retorna a lista de todos os artistas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArtistaResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content)
    })
    @Transactional(readOnly = true)
    @GetMapping("/all")
    public ResponseEntity<List<ArtistaResponse>> findAll() {
        List<Artista> models = service.findAll();
        return ResponseEntity.ok(models.stream().map(mapper::modelToResponse).toList());
    }

    @Operation(
            summary = "Buscar artista por ID",
            description = "Retorna um artista pelo ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista encontrado",
                    content = @Content(schema = @Schema(implementation = ArtistaResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado", content = @Content)
    })
    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public ResponseEntity<ArtistaResponse> findById(
            @Parameter(description = "ID do artista", example = "1", required = true)
            @PathVariable Long id
    ) {
        Artista model = service.findById(id);
        return ResponseEntity.ok(mapper.modelToResponse(model));
    }

}
