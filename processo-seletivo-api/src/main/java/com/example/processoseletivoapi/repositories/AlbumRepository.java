package com.example.processoseletivoapi.repositories;

import com.example.processoseletivoapi.models.Album;
import com.example.processoseletivoapi.models.Artista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Query(value = """
                select a.*
                from tb_albuns a
                where
                  :possuiCantores is null
                  or (
                    (:possuiCantores = true and exists (
                      select 1
                      from tb_albuns_artistas aa
                      where aa.id_album = a.id
                    ))
                    or
                    (:possuiCantores = false and not exists (
                      select 1
                      from tb_albuns_artistas aa
                      where aa.id_album = a.id
                    ))
                  )
            """, nativeQuery = true)
    Page<Album> find(Boolean possuiCantores, Pageable pageable);


}
