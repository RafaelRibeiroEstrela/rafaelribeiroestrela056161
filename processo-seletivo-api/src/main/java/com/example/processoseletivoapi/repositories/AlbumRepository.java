package com.example.processoseletivoapi.repositories;

import com.example.processoseletivoapi.models.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Query(value = """
                SELECT a.*
                   FROM tb_albuns a
                   WHERE
                     (
                       :possuiArtistas IS NULL
                       OR (:possuiArtistas = TRUE  AND EXISTS (
                             SELECT 1
                             FROM tb_albuns_artistas aa
                             WHERE aa.id_album = a.id
                          ))
                       OR (:possuiArtistas = FALSE AND NOT EXISTS (
                             SELECT 1
                             FROM tb_albuns_artistas aa
                             WHERE aa.id_album = a.id
                          ))
                     )
                     AND
                     (
                       :nomeArtista IS NULL
                       OR EXISTS (
                           SELECT 1
                           FROM tb_albuns_artistas aa
                           JOIN tb_artistas ar ON ar.id = aa.id_artista
                           WHERE aa.id_album = a.id
                             AND ar.nome ILIKE '%' || :nomeArtista || '%'
                       )
                     )                           
            """, nativeQuery = true)
    Page<Album> find(Boolean possuiArtistas, String nomeArtista, Pageable pageable);


}
