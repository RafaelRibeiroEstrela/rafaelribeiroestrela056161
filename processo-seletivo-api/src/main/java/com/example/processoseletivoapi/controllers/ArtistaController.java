package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.mappers.ArtistaMapper;
import com.example.processoseletivoapi.models.Artista;
import com.example.processoseletivoapi.requests.ArtistaRequest;
import com.example.processoseletivoapi.responses.ArtistaResponse;
import com.example.processoseletivoapi.services.ArtistaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/artistas")
public class ArtistaController {

    private final ArtistaService service;
    private final ArtistaMapper mapper;

    public ArtistaController(ArtistaService service, ArtistaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<ArtistaResponse> create(@RequestBody ArtistaRequest request) {
        Artista model = service.create(mapper.requestToModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.modelToResponse(model));
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<ArtistaResponse> update(@RequestBody ArtistaRequest request, @PathVariable Long id) {
        Artista model = service.update(mapper.requestToModel(request), id);
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
    public ResponseEntity<List<ArtistaResponse>> findAll() {
        List<Artista> models = service.findAll();
        return ResponseEntity.ok().body(models.stream().map(mapper::modelToResponse).toList());
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public ResponseEntity<ArtistaResponse> findById(@PathVariable Long id) {
        Artista model = service.findById(id);
        return ResponseEntity.ok().body(mapper.modelToResponse(model));
    }

}
