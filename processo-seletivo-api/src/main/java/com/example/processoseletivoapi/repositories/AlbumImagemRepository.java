package com.example.processoseletivoapi.repositories;

import com.example.processoseletivoapi.models.AlbumImagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface AlbumImagemRepository extends JpaRepository<AlbumImagem, Long> {

    List<AlbumImagem> findByAlbumId(Long albumId);

    Optional<AlbumImagem> findByStorageKey(String storageKey);

    @Modifying
    void deleteByAlbumId(Long albumId);

    @Modifying
    void deleteByStorageKey(String key);
}
