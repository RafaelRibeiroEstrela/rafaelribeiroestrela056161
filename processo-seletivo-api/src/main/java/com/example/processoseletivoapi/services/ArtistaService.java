package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.models.Artista;
import com.example.processoseletivoapi.models.projections.ArtistaProjection;
import com.example.processoseletivoapi.repositories.ArtistaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ArtistaService {

    private final ArtistaRepository repository;
    private final AlbumArtistaService albumArtistaService;

    public ArtistaService(ArtistaRepository repository, AlbumArtistaService albumArtistaService) {
        this.repository = repository;
        this.albumArtistaService = albumArtistaService;
    }


    public Artista create(Artista model, Set<Long> listaAlbumId) {
        model = repository.save(model);
        albumArtistaService.updateForArtista(model.getId(), listaAlbumId);
        return model;
    }

    public Artista update(Artista model, Set<Long> listaAlbumId, long id) {
        model.setId(id);
        model = repository.save(model);
        albumArtistaService.updateForArtista(id, listaAlbumId);
        return model;
    }

    public void delete(long id) {
        albumArtistaService.deleteForArtista(id);
        repository.deleteById(id);
    }

    public Page<ArtistaProjection> find(String nomeArtista, Pageable pageable) {
        nomeArtista = nomeArtista == null || nomeArtista.trim().isEmpty() ? null : "%" + nomeArtista + "%";
        return repository.find(nomeArtista, pageable);
    }

    public Artista findById(long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nenhum artista encontrado"));
    }
}
