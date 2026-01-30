package com.example.processoseletivoapi.repositories;

import com.example.processoseletivoapi.models.Artista;
import com.example.processoseletivoapi.models.projections.ArtistaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {

    @Query("SELECT a.id, a.nome, (SELECT COUNT(aa.id.albumId) FROM AlbumArtista aa WHERE aa.id.artistaId = a.id) as quantidadeDeAlbuns " +
            "FROM Artista a " +
            "WHERE (:nomeArtista IS NULL OR UPPER(a.nome) LIKE UPPER(:nomeArtista)) ")
    Page<ArtistaProjection> find(String nomeArtista, Pageable pageable);
}
