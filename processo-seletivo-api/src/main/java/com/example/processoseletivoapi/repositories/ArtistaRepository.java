package com.example.processoseletivoapi.repositories;

import com.example.processoseletivoapi.models.Artista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {

    @Query("SELECT a " +
            "FROM Artista a " +
            "WHERE (:nomeArtista IS NULL OR UPPER(a.nome) LIKE UPPER(:nomeArtista)) ")
    Page<Artista> find(String nomeArtista, Pageable pageable);
}
