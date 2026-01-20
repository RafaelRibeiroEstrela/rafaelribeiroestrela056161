package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.mappers.UserMapper;
import com.example.processoseletivoapi.models.Role;
import com.example.processoseletivoapi.models.User;
import com.example.processoseletivoapi.requests.UserRequest;
import com.example.processoseletivoapi.responses.UserResponse;
import com.example.processoseletivoapi.services.RoleService;
import com.example.processoseletivoapi.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

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

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest request) {
        User model = service.create(mapper.requestToModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.modelToResponse(model));
    }

    @PutMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String username) {
        service.updatePassword(oldPassword, newPassword, username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<UserResponse> findByUsername(@RequestParam String username) {
        User model = service.findByUsername(username);
        Set<Role> roles = roleService.findAllByIds(Arrays.stream(model.getRoles()).collect(Collectors.toSet()));
        return ResponseEntity.ok().body(mapper.modelToResponse(model, roles));
    }
}
