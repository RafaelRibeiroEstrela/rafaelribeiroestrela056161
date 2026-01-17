package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.mappers.AlbumImagemMapper;
import com.example.processoseletivoapi.models.AlbumImagem;
import com.example.processoseletivoapi.responses.AlbumImagemResponse;
import com.example.processoseletivoapi.services.AlbumImagemService;
import com.example.processoseletivoapi.utils.AlbumImagemUtil;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/v1/albuns/imagens")
public class AlbumImagemController {

    private final AlbumImagemService service;
    private final AlbumImagemMapper mapper;
    private final AlbumImagemUtil util;

    public AlbumImagemController(AlbumImagemService service, AlbumImagemMapper mapper, AlbumImagemUtil util) {
        this.service = service;
        this.mapper = mapper;
        this.util = util;
    }

    @Transactional
    @PutMapping("/upload/{albumId}")
    public ResponseEntity<List<AlbumImagemResponse>> uploadImagens(
            @Parameter(description = "Lista de imagens para capas de um album", required = true)
            @RequestParam("files") List<MultipartFile> files,
            @Parameter(description = "ID do album", required = true)
            @PathVariable Long albumId) {
        List<AlbumImagem> models = files.stream()
                .map(obj -> service.upload(mapper.multipartFileToModel(obj, albumId)))
                .toList();
        return ResponseEntity.ok().body(models.stream().map(mapper::modelToResponse).toList());
    }

    @Transactional(readOnly = true)
    @GetMapping("/download/{albumId}")
    public ResponseEntity<Resource> download(
            @Parameter(description = "ID do album", required = true)
            @PathVariable Long albumId) {
        List<AlbumImagem> models = service.downloadByAlbumId(albumId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"arquivos.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(new ByteArrayInputStream(util.compactFilesToZip(models))));
    }
}
