package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.requests.LoginRequest;
import com.example.processoseletivoapi.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AUTHENTICATION")
@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = service.login(request.username(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader String token) {
        service.logout(token);
        return ResponseEntity.ok().build();
    }
}
