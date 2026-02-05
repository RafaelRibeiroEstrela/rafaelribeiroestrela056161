package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.models.AlbumArtista;
import com.example.processoseletivoapi.repositories.AlbumArtistaRepository;
import com.example.processoseletivoapi.repositories.AlbumRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AlbumArtistaService {

    private final AlbumArtistaRepository repository;
    private final AlbumRepository albumRepository;

    public AlbumArtistaService(AlbumArtistaRepository repository, AlbumRepository albumRepository) {
        this.repository = repository;
        this.albumRepository = albumRepository;
    }

    public void updateForArtista(long artistaId, Set<Long> listaAlbumId) {
        if (listaAlbumId == null || listaAlbumId.isEmpty()) {
            repository.deleteByArtistaId(artistaId);
        } else {
            List<AlbumArtista> listaAlbumArtista = repository.findByArtistaId(artistaId);
            List<AlbumArtista> listaAlbumArtistaParaDeletar = new ArrayList<>();
            List<AlbumArtista> listaAlbumArtistaParaSalvar = new ArrayList<>();
            for (AlbumArtista albumArtista : listaAlbumArtista) {
                if (!listaAlbumId.contains(albumArtista.getId().getAlbumId())) {
                    listaAlbumArtistaParaDeletar.add(albumArtista);
                } else {
                    listaAlbumArtistaParaSalvar.add(albumArtista);
                }
            }
            for (Long albumId : listaAlbumId) {
                if (!albumRepository.existsById(albumId)) {
                    throw new ResourceNotFoundException("Nenhum album encontrado com id = " + albumId);
                }
                AlbumArtista albumArtista = new AlbumArtista(artistaId, albumId);
                if (!listaAlbumArtistaParaSalvar.contains(albumArtista)) {
                    listaAlbumArtistaParaSalvar.add(albumArtista);
                }
            }
            repository.saveAll(listaAlbumArtistaParaSalvar);
            repository.deleteAllById(listaAlbumArtistaParaDeletar.stream().map(AlbumArtista::getId).toList());
        }
    }

    public void deleteForArtista(long artistaId) {
        repository.deleteByArtistaId(artistaId);
    }
}
