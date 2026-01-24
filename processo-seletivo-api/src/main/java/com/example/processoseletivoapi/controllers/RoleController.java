package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.mappers.RoleMapper;
import com.example.processoseletivoapi.models.Role;
import com.example.processoseletivoapi.requests.RoleRequest;
import com.example.processoseletivoapi.responses.RoleResponse;
import com.example.processoseletivoapi.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ROLE")
@RestController
@RequestMapping("/v1/roles")
public class RoleController {

    private final RoleService service;
    private final RoleMapper mapper;

    public RoleController(RoleService service, RoleMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Criar role",
            description = "Cria uma nova role no sistema.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Role criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito (ex.: role já existe)", content = @Content)
    })
    @Transactional
    @PostMapping
    public ResponseEntity<RoleResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criação da role",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RoleRequest.class))
            )
            @RequestBody RoleRequest request
    ) {
        Role model = service.create(mapper.requestToModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.modelToResponse(model));
    }

    @Operation(
            summary = "Atualizar role",
            description = "Atualiza os dados de uma role existente pelo ID.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content),
            @ApiResponse(responseCode = "404", description = "Role não encontrada", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito", content = @Content)
    })
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para atualização da role",
                    required = true
            )
            @RequestBody RoleRequest request,
            @Parameter(description = "ID da role", required = true)
            @PathVariable Long id
    ) {
        Role model = service.update(mapper.requestToModel(request), id);
        return ResponseEntity.ok(mapper.modelToResponse(model));
    }

    @Operation(
            summary = "Excluir role",
            description = "Remove uma role pelo ID.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Role removida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content),
            @ApiResponse(responseCode = "404", description = "Role não encontrada", content = @Content)
    })
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da role", required = true)
            @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar roles",
            description = "Lista todas as roles cadastradas.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content)
    })
    @Transactional(readOnly = true)
    @GetMapping("/all")
    public ResponseEntity<List<RoleResponse>> findAll() {
        List<Role> models = service.findAll();
        return ResponseEntity.ok(models.stream().map(mapper::modelToResponse).toList());
    }

}
