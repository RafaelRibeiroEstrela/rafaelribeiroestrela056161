package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.BusinessException;
import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.models.AlbumArtista;
import com.example.processoseletivoapi.models.Artista;
import com.example.processoseletivoapi.repositories.AlbumArtistaRepository;
import com.example.processoseletivoapi.repositories.AlbumRepository;
import com.example.processoseletivoapi.repositories.ArtistaRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ArtistaService {

    private final ArtistaRepository repository;
    private final AlbumRepository albumRepository;
    private final AlbumArtistaRepository albumArtistaRepository;

    public ArtistaService(ArtistaRepository repository, AlbumRepository albumRepository, AlbumArtistaRepository albumArtistaRepository) {
        this.repository = repository;
        this.albumRepository = albumRepository;
        this.albumArtistaRepository = albumArtistaRepository;
    }

    public Artista create(Artista model, Set<Long> listaAlbumId) {
        model = repository.save(model);
        if (listaAlbumId != null && !listaAlbumId.isEmpty()) {
            Set<AlbumArtista> albumArtistas = new HashSet<>();
            for (Long albumId : listaAlbumId) {
                if (!albumRepository.existsById(albumId)) {
                    throw new BusinessException("Nenhum album encontrado com id = " + albumId);
                }
                albumArtistas.add(new AlbumArtista(model.getId(), albumId));
            }
            albumArtistaRepository.saveAll(albumArtistas);
        }
        return model;
    }

    public Artista update(Artista model, Set<Long> listaAlbumId, long id) {
        model.setId(id);
        model = repository.save(model);
        if (listaAlbumId == null || listaAlbumId.isEmpty()) {
            albumArtistaRepository.deleteByArtistaId(id);
        } else {
            List<AlbumArtista> listaAlbumArtista = albumArtistaRepository.findByArtistaId(id);
            listaAlbumArtista.removeIf(obj -> !listaAlbumId.contains(obj.getId().getAlbumId()));
            for (Long albumId : listaAlbumId) {
                if (!albumRepository.existsById(albumId)) {
                    throw new BusinessException("Nenhum album encontrado com id = " + albumId);
                }
                AlbumArtista albumArtista = new AlbumArtista(id, albumId);
                if (!listaAlbumArtista.contains(albumArtista)) {
                    listaAlbumArtista.add(albumArtista);
                }
            }
            albumArtistaRepository.saveAll(listaAlbumArtista);
        }
        return model;
    }

    public void delete(long id) {
        albumArtistaRepository.deleteByArtistaId(id);
        repository.deleteById(id);
    }

    public List<Artista> findAll() {
        return repository.findAll();
    }

    public Artista findById(long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nenhum artista encontrado"));
    }
}
