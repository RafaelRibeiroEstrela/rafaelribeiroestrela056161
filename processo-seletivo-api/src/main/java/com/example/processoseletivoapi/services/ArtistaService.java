package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.models.AlbumArtista;
import com.example.processoseletivoapi.models.Artista;
import com.example.processoseletivoapi.repositories.AlbumArtistaRepository;
import com.example.processoseletivoapi.repositories.ArtistaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistaService {

    private final ArtistaRepository repository;
    private final AlbumArtistaRepository albumArtistaRepository;

    public ArtistaService(ArtistaRepository repository, AlbumArtistaRepository albumArtistaRepository) {
        this.repository = repository;
        this.albumArtistaRepository = albumArtistaRepository;
    }

    public Artista create(Artista model) {
        return repository.save(model);
    }

    public Artista update(Artista model, long id) {
        model.setId(id);
        return repository.save(model);
    }

    public void delete(long id) {
        albumArtistaRepository.deleteByArtistaId(id);
        repository.deleteById(id);
    }

    public List<Artista> findAll() {
        return repository.findAll();
    }

    public List<Artista> findByAlbumId(long albumId) {
        List<AlbumArtista> albumArtistaList = albumArtistaRepository.findByAlbumId(albumId);
        return repository.findAllById(albumArtistaList.stream().map(obj -> obj.getId().getArtistaId()).toList());
    }

    public Artista findById(long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nenhum artista encontrado"));
    }
}
