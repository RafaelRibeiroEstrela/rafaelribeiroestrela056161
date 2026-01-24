package com.example.processoseletivoapi.controllers;

import com.example.processoseletivoapi.mappers.AlbumMapper;
import com.example.processoseletivoapi.mappers.ArtistaMapper;
import com.example.processoseletivoapi.models.Album;
import com.example.processoseletivoapi.models.Artista;
import com.example.processoseletivoapi.requests.AlbumRequest;
import com.example.processoseletivoapi.responses.AlbumResponse;
import com.example.processoseletivoapi.services.AlbumService;
import com.example.processoseletivoapi.services.ArtistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "ALBUM")
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
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Criar álbum",
            description = "Cria um álbum e associa os artistas informados. Publica o evento no WebSocket."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Álbum criado"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Artista(s) não encontrado(s)", content = @Content)
    })
    public ResponseEntity<AlbumResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados do álbum",
                    content = @Content(schema = @Schema(implementation = AlbumRequest.class))
            )
            @RequestBody AlbumRequest request
    ) {
        Album model = service.create(mapper.requestToModel(request), request.artistaIdList());
        webSoket.publicar(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.modelToResponse(model));
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Atualizar álbum",
            description = "Atualiza os dados do álbum e pode atualizar os artistas associados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Álbum atualizado"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Álbum (ou artista) não encontrado", content = @Content)
    })
    public ResponseEntity<AlbumResponse> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados do álbum",
                    content = @Content(schema = @Schema(implementation = AlbumRequest.class))
            )
            @RequestBody AlbumRequest request,

            @Parameter(description = "ID do álbum", example = "10", required = true)
            @PathVariable Long id
    ) {
        Album model = service.update(mapper.requestToModel(request), id, request.artistaIdList());
        return ResponseEntity.ok().body(mapper.modelToResponse(model));
    }

    @Transactional
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir álbum", description = "Remove um álbum pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Álbum removido"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do álbum", example = "10", required = true)
            @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional(readOnly = true)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Listar álbuns (com filtros, ordenação e paginação)",
            description = """
                    Retorna álbuns de forma paginada.
                    Filtros opcionais:
                    - possuiCantor: filtra se o álbum possui (ou não) cantores/artistas vinculados
                    - nomeArtista: filtra por nome do artista (ex.: busca parcial)
                    Ordenação:
                    - ordenacao: ASC ou DESC (direção)
                    Paginação:
                    - page, size, sort (padrão do Spring Pageable)
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de álbuns")
    })
    public ResponseEntity<Page<AlbumResponse>> find(
            @Parameter(description = "Se TRUE, retorna apenas álbuns que possuem artistas; se FALSE, apenas os que não possuem; se null, ignora o filtro")
            @RequestParam(required = false) Boolean possuiCantor,

            @Parameter(description = "Filtra por nome do artista (parcial). Se null, ignora o filtro")
            @RequestParam(required = false) String nomeArtista,

            @Parameter(description = "Pagina inicial da paginação")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Total de elementos que serão recuperados para a página")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Ordenação alfabetica dos albuns com direção da ordenação (ASC/DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction ordenacao
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(ordenacao, "nome"));
        Page<Album> albuns = service.find(possuiCantor, nomeArtista, pageable);
        return ResponseEntity.ok().body(albuns.map(mapper::modelToResponse));
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Buscar álbum por ID",
            description = "Retorna o álbum e os artistas associados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Álbum encontrado"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content)
    })
    public ResponseEntity<AlbumResponse> findById(
            @Parameter(description = "ID do álbum", example = "10", required = true)
            @PathVariable Long id
    ) {
        Album model = service.findById(id);
        List<Artista> artistas = artistaService.findByAlbumId(id);

        return ResponseEntity.ok().body(
                mapper.modelToResponse(
                        model,
                        artistas.stream()
                                .map(artistaMapper::modelToResponse)
                                .collect(Collectors.toSet())
                )
        );
    }

    /**
     * Wrapper somente para documentação do Swagger:
     * Page<AlbumResponse> é genérico e o Swagger costuma renderizar "Page" sem o conteúdo tipado.
     */
    @Schema(name = "PageAlbumResponse", description = "Estrutura padrão de paginação contendo itens de AlbumResponse")
    static class PageAlbumResponse extends org.springframework.data.domain.PageImpl<AlbumResponse> {
        public PageAlbumResponse() {
            super(List.of());
        }
    }
}
