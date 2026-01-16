package com.example.processoseletivoapi.models;

import com.example.processoseletivoapi.exceptions.BusinessException;
import com.example.processoseletivoapi.models.ids.AlbumArtistaId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_albuns_artistas")
public class AlbumArtista {

    @EmbeddedId
    private AlbumArtistaId id;

    public AlbumArtista() {
    }

    public AlbumArtista(Long artistaId, Long albumId) {
        this.setId(artistaId, albumId);
    }

    public AlbumArtistaId getId() {
        return id;
    }

    public void setId(AlbumArtistaId id) {
        this.id = id;
    }

    public void setId(Long artistaId, Long albumId) {
        if (artistaId == null || albumId == null) {
            throw new BusinessException("O identificador do artista e do album é obrigatório");
        }
        this.id = new AlbumArtistaId(artistaId, albumId);
    }
}
