package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.mappers.UserMapper;
import com.example.processoseletivoapi.models.Role;
import com.example.processoseletivoapi.models.User;
import com.example.processoseletivoapi.requests.UserRequest;
import com.example.processoseletivoapi.responses.UserResponse;
import com.example.processoseletivoapi.services.RoleService;
import com.example.processoseletivoapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "USER")
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService service;
    private final UserMapper mapper;
    private final RoleService roleService;

    public UserController(UserService service, UserMapper mapper, RoleService roleService) {
        this.service = service;
        this.mapper = mapper;
        this.roleService = roleService;
    }

    @Operation(
            summary = "Criar usuário",
            description = "Cria um novo usuário e retorna o usuário criado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "409", description = "Usuário já existe", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criação do usuário",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRequest.class))
            )
            @RequestBody UserRequest request
    ) {
        User model = service.create(mapper.requestToModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.modelToResponse(model));
    }

    @Operation(
            summary = "Atualizar roles do usuário",
            description = "Atualiza o conjunto de roles do usuário informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Roles atualizadas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @PutMapping("/update-roles")
    public ResponseEntity<Void> updateRoles(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "IDs das roles a serem atribuídas ao usuário",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Long.class))
            )
            @RequestBody Set<Long> rolesId,
            @Parameter(description = "Username do usuário", required = true, example = "joao.silva")
            @RequestParam String username
    ) {
        service.updateRoles(rolesId, username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Atualizar senha do usuário",
            description = "Altera a senha do usuário, validando a senha antiga."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Senha antiga inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @PutMapping("/update-password")
    public ResponseEntity<Void> updatePassword(
            @Parameter(description = "Senha atual", required = true, example = "SenhaAntiga@123")
            @RequestParam String oldPassword,
            @Parameter(description = "Nova senha", required = true, example = "NovaSenha@123")
            @RequestParam String newPassword,
            @Parameter(description = "Username do usuário", required = true, example = "joao.silva")
            @RequestParam String username
    ) {
        service.updatePassword(oldPassword, newPassword, username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Excluir usuário",
            description = "Remove o usuário pelo ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do usuário", required = true, example = "10")
            @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar usuário por username",
            description = "Retorna os dados do usuário e suas roles a partir do username."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping
    public ResponseEntity<UserResponse> findByUsername(
            @Parameter(description = "Username do usuário", required = true, example = "admin")
            @RequestParam String username
    ) {
        User model = service.findByUsername(username);
        Set<Role> roles = roleService.findAllByIds(Arrays.stream(model.getRoles()).collect(Collectors.toSet()));
        return ResponseEntity.ok().body(mapper.modelToResponse(model, roles));
    }
}
