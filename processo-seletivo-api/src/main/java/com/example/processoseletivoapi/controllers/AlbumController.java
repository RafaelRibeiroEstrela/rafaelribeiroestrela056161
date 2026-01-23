package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.mappers.AlbumMapper;
import com.example.processoseletivoapi.mappers.ArtistaMapper;
import com.example.processoseletivoapi.models.Album;
import com.example.processoseletivoapi.models.Artista;
import com.example.processoseletivoapi.requests.AlbumRequest;
import com.example.processoseletivoapi.responses.AlbumResponse;
import com.example.processoseletivoapi.services.AlbumService;
import com.example.processoseletivoapi.services.ArtistaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/albuns")
public class AlbumController {

    private final AlbumService service;
    private final ArtistaService artistaService;
    private final AlbumMapper mapper;
    private final ArtistaMapper artistaMapper;
    private final AlbumWebSoket webSoket;

    public AlbumController(AlbumService service, ArtistaService artistaService, AlbumMapper mapper, ArtistaMapper artistaMapper, AlbumWebSoket webSoket) {
        this.service = service;
        this.artistaService = artistaService;
        this.mapper = mapper;
        this.artistaMapper = artistaMapper;
        this.webSoket = webSoket;
    }


    @Transactional
    @PostMapping
    public ResponseEntity<AlbumResponse> create(@RequestBody AlbumRequest request) {
        Album model = service.create(mapper.requestToModel(request), request.artistaIdList());
        webSoket.publicar(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.modelToResponse(model));
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponse> update(@RequestBody AlbumRequest request, @PathVariable Long id) {
        Album model = service.update(mapper.requestToModel(request), id, request.artistaIdList());
        return ResponseEntity.ok().body(mapper.modelToResponse(model));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional(readOnly = true)
    @GetMapping
    public ResponseEntity<Page<AlbumResponse>> find(@RequestParam(required = false) Boolean possuiCantor, @RequestParam(required = false) String nomeArtista, Sort.Direction ordenacao, Pageable pageable) {
        Page<Album> albuns = service.find(possuiCantor, nomeArtista, ordenacao, pageable);
        return ResponseEntity.ok().body(albuns.map(mapper::modelToResponse));
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponse> findById(@PathVariable Long id) {
        Album model = service.findById(id);
        List<Artista> artistas = artistaService.findByAlbumId(id);
        return ResponseEntity.ok().body(mapper.modelToResponse(model, artistas.stream().map(artistaMapper::modelToResponse).collect(Collectors.toSet())));
    }
}
