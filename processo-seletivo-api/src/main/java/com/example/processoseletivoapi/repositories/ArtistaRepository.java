package com.example.processoseletivoapi.repositories;

import com.example.processoseletivoapi.models.Artista;
import com.example.processoseletivoapi.models.projections.ArtistaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {

    @Query(
            value = """
        select
            a.id as id,
            a.nome as nome,
            (
                select count(aa.id_album)
                from tb_albuns_artistas aa
                where aa.id_artista = a.id
            ) as quantidadeDeAlbuns
        from tb_artistas a
        where (:nomeArtista is null
               or a.nome ilike :nomeArtista)
        """,
            countQuery = """
        select count(a.id)
        from tb_artistas a
        where (:nomeArtista is null
               or a.nome ilike :nomeArtista)
        """,
            nativeQuery = true
    )
    Page<ArtistaProjection> find(@Param("nomeArtista") String nomeArtista, Pageable pageable);

}
