package com.example.processoseletivoapi.models.ids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class AlbumArtistaId {

    @Column(name = "id_artista")
    private Long artistaId;
    @Column(name = "id_album")
    private Long albumId;

    public AlbumArtistaId() {}

    public AlbumArtistaId(Long artistaId, Long albumId) {
        this.artistaId = artistaId;
        this.albumId = albumId;
    }

    public Long getArtistaId() {
        return artistaId;
    }

    public void setArtistaId(Long artistaId) {
        this.artistaId = artistaId;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AlbumArtistaId that = (AlbumArtistaId) o;
        return Objects.equals(artistaId, that.artistaId) && Objects.equals(albumId, that.albumId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistaId, albumId);
    }
}
