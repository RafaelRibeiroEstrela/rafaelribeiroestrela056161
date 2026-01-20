package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.mappers.RoleMapper;
import com.example.processoseletivoapi.models.Role;
import com.example.processoseletivoapi.requests.RoleRequest;
import com.example.processoseletivoapi.responses.RoleResponse;
import com.example.processoseletivoapi.services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/roles")
public class RoleController {

    private final RoleService service;
    private final RoleMapper mapper;

    public RoleController(RoleService service, RoleMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<RoleResponse> create(@RequestBody RoleRequest request) {
        Role model = service.create(mapper.requestToModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.modelToResponse(model));
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> update(@RequestBody RoleRequest request, @PathVariable Long id) {
        Role model = service.update(mapper.requestToModel(request), id);
        return ResponseEntity.ok().body(mapper.modelToResponse(model));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional(readOnly = true)
    @GetMapping("/all")
    public ResponseEntity<List<RoleResponse>> findAll() {
        List<Role> models = service.findAll();
        return ResponseEntity.ok().body(models.stream().map(mapper::modelToResponse).toList());
    }

}
