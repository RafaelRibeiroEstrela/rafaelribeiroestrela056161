package com.example.processoseletivoapi.repositories;

import com.example.processoseletivoapi.models.AlbumImagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumImagemRepository extends JpaRepository<AlbumImagem, Long> {

    List<AlbumImagem> findByAlbumId(Long albumId);

    Optional<AlbumImagem> findByStorageKey(String storageKey);
}
