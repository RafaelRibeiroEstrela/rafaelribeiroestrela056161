package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.requests.LoginRequest;
import com.example.processoseletivoapi.services.AuthenticationService;
import com.example.processoseletivoapi.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AUTENTICACAO")
@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;
    private final TokenService tokenService;

    public AuthenticationController(AuthenticationService service, TokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    @Operation(
            summary = "Realiza login e retorna o token",
            description = "Autentica o usuário e retorna um token de acesso."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Login realizado com sucesso (token retornado)"
    )
    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content)
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais de acesso",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(value = """
                                    { "username": "usuario", "password": "senha" }
                                    """)
                    )
            )
            @RequestBody LoginRequest request
    ) {
        String token = service.login(request.username(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @Operation(
            summary = "Realiza logout",
            description = "Invalida o token informado (ex.: blacklist, remoção de sessão, etc.).",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso", content = @Content)
    @ApiResponse(responseCode = "401", description = "Token ausente ou inválido", content = @Content)
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Parameter(
                    description = "Token no formato: Bearer <token>",
                    example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            )
            @RequestHeader("Authorization") String token
    ) {
        service.logout(token);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Realiza atualização de token"
    )
    @ApiResponse(responseCode = "200", description = "Token atualizado com sucesso", content = @Content)
    @ApiResponse(responseCode = "401", description = "Token ausente ou inválido", content = @Content)
    @PutMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(
            @Parameter(
                    description = "Token antigo"
            )
            @RequestHeader("token") String token
    ) {
        String newToken = tokenService.refreshToken(token);
        return ResponseEntity.ok().body(newToken);
    }
}
