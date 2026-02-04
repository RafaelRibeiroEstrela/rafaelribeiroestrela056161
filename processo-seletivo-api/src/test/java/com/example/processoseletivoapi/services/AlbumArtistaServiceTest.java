package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.models.AlbumArtista;
import com.example.processoseletivoapi.repositories.AlbumArtistaRepository;
import com.example.processoseletivoapi.repositories.AlbumRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class AlbumArtistaServiceTest {

    @InjectMocks
    private AlbumArtistaService service;

    @Mock
    private AlbumArtistaRepository repository;

    @Mock
    private AlbumRepository albumRepository;

    @Test
    void testAtualizarParaArtistaComSucessoQuandoListaAlbumIdEstaVazio() {
        Set<Long> listaAlbumId = Set.of();
        long artistaId = 1L;
        Assertions.assertAll(() -> service.updateForArtista(artistaId, listaAlbumId));
        Set<Long> listaAlbumId2 = null;
        Assertions.assertAll(() -> service.updateForArtista(artistaId, listaAlbumId2));
    }

    @Test
    void testAtualizarParaArtistaComSucessoQuandoNenhumAlbumEncontradoParaArtista() {
        Set<Long> listaAlbumId = Set.of(1L);
        long artistaId = 1L;
        List<AlbumArtista> listaAlbumArtista = new ArrayList<>();
        Mockito.when(repository.findByArtistaId(artistaId)).thenReturn(listaAlbumArtista);
        Mockito.when(albumRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Assertions.assertAll(() -> service.updateForArtista(artistaId, listaAlbumId));
    }

    @Test
    void testAtualizarParaArtistaComSucessoQuandoAlbumEncontradoParaArtista() {
        Set<Long> listaAlbumId = Set.of(1L);
        long artistaId = 1L;
        List<AlbumArtista> listaAlbumArtista = new ArrayList<>(List.of(new AlbumArtista(1L, 1L)));
        Mockito.when(repository.findByArtistaId(artistaId)).thenReturn(listaAlbumArtista);
        Mockito.when(albumRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Assertions.assertAll(() -> service.updateForArtista(artistaId, listaAlbumId));
    }
}
