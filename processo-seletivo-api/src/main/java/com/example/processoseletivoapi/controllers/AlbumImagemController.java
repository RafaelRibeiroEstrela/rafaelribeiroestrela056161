package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.mappers.AlbumImagemMapper;
import com.example.processoseletivoapi.models.AlbumImagem;
import com.example.processoseletivoapi.responses.AlbumImagemResponse;
import com.example.processoseletivoapi.services.AlbumImagemService;
import com.example.processoseletivoapi.utils.AlbumImagemUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "ALBUM - IMAGEM")
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

    @Operation(
            summary = "Upload de imagens do álbum",
            description = "Recebe uma ou mais imagens (multipart/form-data) e associa ao álbum informado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Imagens enviadas com sucesso"
            ),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content),
            @ApiResponse(responseCode = "413", description = "Arquivo(s) muito grande(s)", content = @Content)
    })
    @Transactional
    @PutMapping(value = "/upload/{albumId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<AlbumImagemResponse>> upload(
            @Parameter(
                    description = "Lista de arquivos (imagens) para upload",
                    required = true
            )
            @RequestParam("files") List<MultipartFile> files,

            @Parameter(description = "ID do álbum", required = true)
            @PathVariable Long albumId) {

        List<AlbumImagem> models = files.stream()
                .map(obj -> service.upload(mapper.multipartFileToModel(obj, albumId)))
                .toList();

        return ResponseEntity.ok(models.stream().map(mapper::modelToResponse).toList());
    }

    @Operation(
            summary = "Download das imagens do álbum (ZIP)",
            description = "Baixa um arquivo .zip com todas as imagens associadas ao álbum."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "ZIP gerado com sucesso"
            ),
            @ApiResponse(responseCode = "404", description = "Álbum/imagens não encontrados", content = @Content)
    })
    @Transactional(readOnly = true)
    @GetMapping("/download/{albumId}")
    public ResponseEntity<Resource> download(
            @Parameter(description = "ID do álbum", required = true, in = ParameterIn.PATH)
            @PathVariable Long albumId) {

        List<AlbumImagem> models = service.downloadByAlbumId(albumId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"capas-album.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(new ByteArrayInputStream(util.compactFilesToZip(models))));
    }

    @Operation(
            summary = "Recuperar imagens do álbum (BASE64)",
            description = "Recuperar arquivos em base64 das imagens associadas ao álbum."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Arquivos recuperados com sucesso"
            ),
            @ApiResponse(responseCode = "404", description = "Álbum/imagens não encontrados", content = @Content)
    })
    @Transactional(readOnly = true)
    @GetMapping("/download/base64/{albumId}")
    public ResponseEntity<List<AlbumImagemResponse>> downloadBase64(
            @Parameter(description = "ID do álbum", required = true, in = ParameterIn.PATH)
            @PathVariable Long albumId) {

        List<AlbumImagem> models = service.downloadByAlbumId(albumId);

        return ResponseEntity.ok().body(models.stream().map(mapper::modelToResponse).toList());
    }

    @Operation(
            summary = "Download por storage key",
            description = "Baixa um único arquivo pelo identificador (storage key) no storage."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Arquivo obtido com sucesso"
            ),
            @ApiResponse(responseCode = "404", description = "Arquivo não encontrado", content = @Content)
    })
    @Transactional(readOnly = true)
    @GetMapping("/download/storage-key")
    public ResponseEntity<Resource> downloadByStorageKey(
            @Parameter(description = "Chave do arquivo no storage", required = true, in = ParameterIn.QUERY)
            @RequestParam String key) {

        AlbumImagem model = service.downloadByStorageKey(key);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + model.getFileName())
                .contentType(MediaType.parseMediaType(model.getFileContentType()))
                .body(new InputStreamResource(new ByteArrayInputStream(model.getContent())));
    }

    @Operation(
            summary = "Recuperar metadados das imagens do álbum",
            description = "Retorna somente metadados (ex.: nome, tamanho, tipo, storage key) das imagens do álbum."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Metadados recuperados com sucesso"
            ),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content)
    })
    @Transactional(readOnly = true)
    @GetMapping("/recover-metadata/{albumId}")
    public ResponseEntity<List<AlbumImagemResponse>> recoverMetadataByAlbumId(
            @Parameter(description = "ID do álbum", required = true)
            @PathVariable Long albumId) {

        List<AlbumImagem> models = service.recoverMetadataByAlbumId(albumId);
        return ResponseEntity.ok(models.stream().map(mapper::modelToResponse).toList());
    }

    @Operation(
            summary = "Remover todas as imagens do álbum",
            description = "Remove todas as imagens associadas ao álbum e seus metadados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Imagens removidas com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content)
    })
    @Transactional
    @DeleteMapping("/delete/{albumId}")
    public ResponseEntity<Void> deleteByAlbumId(
            @Parameter(description = "ID do álbum", required = true)
            @PathVariable Long albumId) {

        service.deleteByAlbumId(albumId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Remover imagem por storage key",
            description = "Remove uma imagem específica pela storage key e apaga seus metadados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Imagem removida com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Imagem não encontrada", content = @Content)
    })
    @Transactional
    @DeleteMapping("/delete/storage-key")
    public ResponseEntity<Void> deleteByStorageKey(
            @Parameter(description = "Chave do arquivo no storage", required = true, in = ParameterIn.QUERY)
            @RequestParam String key) {

        service.deleteByStorageKey(key);
        return ResponseEntity.noContent().build();
    }
}
