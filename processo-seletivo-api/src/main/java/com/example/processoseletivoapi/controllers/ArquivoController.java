package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.models.AlbumImagem;
import com.example.processoseletivoapi.services.AlbumImagemService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
public class ArquivoController {

    private final AlbumImagemService service;

    public ArquivoController(AlbumImagemService service) {
        this.service = service;
    }

    @Transactional(readOnly = true)
    //@Hidden
    @GetMapping("/minio/miniobucket/{token}")
    public ResponseEntity<Resource> downloadByStorageKey(
            @Parameter(description = "Chave do arquivo no storage", required = true, in = ParameterIn.QUERY)
            @PathVariable String token) {

        AlbumImagem model = service.downloadLinkPreAssinado(token);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + model.getFileName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(new ByteArrayInputStream(model.getContent())));
    }
}
