package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.models.Album;
import com.example.processoseletivoapi.models.AlbumArtista;
import com.example.processoseletivoapi.repositories.AlbumArtistaRepository;
import com.example.processoseletivoapi.repositories.AlbumImagemRepository;
import com.example.processoseletivoapi.repositories.AlbumRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AlbumService {

    private final AlbumRepository repository;
    private final AlbumArtistaRepository albumArtistaRepository;
    private final AlbumImagemRepository albumImagemRepository;

    public AlbumService(AlbumRepository repository, AlbumArtistaRepository albumArtistaRepository, AlbumImagemRepository albumImagemRepository) {
        this.repository = repository;
        this.albumArtistaRepository = albumArtistaRepository;
        this.albumImagemRepository = albumImagemRepository;
    }

    public Album create(Album model) {
        model = repository.save(model);
        return model;
    }

    public Album update(Album model, long id) {
        model.setId(id);
        model = repository.save(model);
        return model;
    }

    public void delete(long id) {
        albumArtistaRepository.deleteByAlbumId(id);
        albumImagemRepository.deleteByAlbumId(id);
        repository.deleteById(id);
    }

    public Page<Album> find(Boolean possuiCantores, String nomeArtista, Pageable pageable) {
        nomeArtista = nomeArtista == null || nomeArtista.trim().isEmpty() ? null : "%" + nomeArtista + "%";
        return repository.find(possuiCantores, nomeArtista, pageable);
    }

    public Album findById(long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nenhum album encontrado"));
    }

    public Set<Album> findByArtistaId(long artistaId) {
        List<AlbumArtista> listaAlbumArtista = albumArtistaRepository.findByArtistaId(artistaId);
        List<Long> listaAlbumId = listaAlbumArtista.stream().map(obj -> obj.getId().getAlbumId()).toList();
        return new HashSet<>(repository.findAllById(listaAlbumId));
    }
}
