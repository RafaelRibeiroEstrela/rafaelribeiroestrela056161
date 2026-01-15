package com.example.processoseletivoapi.repositories;


import com.example.processoseletivoapi.models.AlbumArtista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlbumArtistaRepository extends JpaRepository<AlbumArtista, Long> {

    @Query("SELECT aa " +
            "FROM AlbumArtista aa " +
            "WHERE aa.id.albumId = :albumId ")
    List<AlbumArtista> findByAlbumId(Long albumId);

    @Modifying
    @Query("DELETE " +
            "FROM AlbumArtista aa " +
            "WHERE aa.id.albumId = :albumId ")
    void deleteByAlbumId(Long albumId);

    @Modifying
    @Query("DELETE " +
            "FROM AlbumArtista aa " +
            "WHERE aa.id.artistaId = :artistaId ")
    void deleteByArtistaId(Long artistaId);
}
