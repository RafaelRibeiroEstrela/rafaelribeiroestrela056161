package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.BusinessException;
import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.models.Album;
import com.example.processoseletivoapi.models.AlbumArtista;
import com.example.processoseletivoapi.repositories.AlbumArtistaRepository;
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

    public AlbumService(AlbumRepository repository, AlbumArtistaRepository albumArtistaRepository) {
        this.repository = repository;
        this.albumArtistaRepository = albumArtistaRepository;
    }

    public Album create(Album model, Set<Long> artistaIdList) {
        model = repository.save(model);
        if (artistaIdList != null && !artistaIdList.isEmpty()) {
            Set<AlbumArtista> albumArtistas = new HashSet<>();
            for (Long artistaId : artistaIdList) {
                albumArtistas.add(new AlbumArtista(artistaId, model.getId()));
            }
            albumArtistaRepository.saveAll(albumArtistas);
        }
        return model;
    }

    public Album update(Album model, long id, Set<Long> artistaIdList) {
        model.setId(id);
        model = repository.save(model);
        if (artistaIdList == null || artistaIdList.isEmpty()) {
            albumArtistaRepository.deleteByAlbumId(id);
        } else {
            List<AlbumArtista> albumArtistaList = albumArtistaRepository.findByAlbumId(id);
            albumArtistaList.removeIf(obj -> !artistaIdList.contains(obj.getId().getArtistaId()));
            for (Long artistaId : artistaIdList) {
                AlbumArtista albumArtista = new AlbumArtista(artistaId, id);
                if (!albumArtistaList.contains(albumArtista)) {
                    albumArtistaList.add(albumArtista);
                }
            }
            albumArtistaRepository.saveAll(albumArtistaList);
        }
        return model;
    }

    public void delete(long id) {
        albumArtistaRepository.deleteByAlbumId(id);
        repository.deleteById(id);
    }

    public Page<Album> find(Boolean possuiCantores, Pageable pageable) {
        return repository.find(possuiCantores, pageable);
    }

    public Album findById(long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nenhum album encontrado"));
    }
}
