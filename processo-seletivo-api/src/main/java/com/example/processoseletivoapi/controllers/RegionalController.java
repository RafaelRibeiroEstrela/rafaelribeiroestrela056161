package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.mappers.RegionalMapper;
import com.example.processoseletivoapi.models.Regional;
import com.example.processoseletivoapi.responses.RegionalResponse;
import com.example.processoseletivoapi.services.RegionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/regionais")
public class RegionalController {

    private final RegionalService service;
    private final RegionalMapper mapper;

    public RegionalController(RegionalService service, RegionalMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/synchronize")
    public ResponseEntity<String> sync() {
        service.sync();
        return ResponseEntity.ok().body("Base de dados sincronizada");
    }

    @GetMapping("/all")
    public ResponseEntity<List<RegionalResponse>> findAll() {
        List<Regional> models = service.findAll();
        return ResponseEntity.ok().body(models.stream().map(mapper::modelToResponse).toList());
    }
}
