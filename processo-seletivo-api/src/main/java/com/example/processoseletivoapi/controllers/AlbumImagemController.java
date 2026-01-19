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
    @PutMapping(value = "/upload/{albumId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<AlbumImagemResponse>> uploadImagens(
            @RequestParam("files") List<MultipartFile> files,
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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"capas-album.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(new ByteArrayInputStream(util.compactFilesToZip(models))));
    }

    @Transactional(readOnly = true)
    @GetMapping("/download/storage-key/{key}")
    public ResponseEntity<Resource> downloadByStorageKey(
            @Parameter(description = "Chave do arquivo", required = true)
            @RequestParam String key) {
        AlbumImagem model = service.downloadByStorageKey(key);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + model.getFileName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(new ByteArrayInputStream(model.getContent())));
    }

    @Transactional(readOnly = true)
    @GetMapping("/recover-metadata/{albumId}")
    public ResponseEntity<List<AlbumImagemResponse>> recoverMetadataByAlbumId(@PathVariable Long albumId) {
        List<AlbumImagem> models = service.recoverMetadataByAlbumId(albumId);
        return ResponseEntity.ok().body(models.stream().map(mapper::modelToResponse).toList());
    }

    @Transactional
    @DeleteMapping("/delete/{albumId}")
    public ResponseEntity<List<AlbumImagemResponse>> deleteByAlbumId(@PathVariable Long albumId) {
        service.deleteByAlbumId(albumId);
        return ResponseEntity.noContent().build();
    }


    @Transactional
    @DeleteMapping("/delete/storage-key/{key}")
    public ResponseEntity<List<AlbumImagemResponse>> deleteByStorageKey(@PathVariable String key) {
        service.deleteByStorageKey(key);
        return ResponseEntity.noContent().build();
    }
}
