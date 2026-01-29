package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.BusinessException;
import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.models.AlbumArtista;
import com.example.processoseletivoapi.models.Artista;
import com.example.processoseletivoapi.repositories.AlbumArtistaRepository;
import com.example.processoseletivoapi.repositories.ArtistaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

/*

@ExtendWith(MockitoExtension.class)
class ArtistaServiceTest {

    @InjectMocks
    private ArtistaService service;

    @Mock
    private ArtistaRepository repository;

    @Mock
    private AlbumArtistaRepository albumArtistaRepository;

    @Test
    void testCriarArtistaComSucesso() {
        Assertions.assertAll(() -> new Artista(null, "Artista teste"));
        Artista artista = new Artista(1L, "Artista teste");
        Assertions.assertAll(() -> service.create(artista));
    }

    @Test
    void testCriarArtistaComErroCampoObrigatorio() {
        Assertions.assertThrows(BusinessException.class, () -> new Artista(null, ""));
        Assertions.assertThrows(BusinessException.class, () -> new Artista(null, null));
    }

    @Test
    void testAtualizarArtistaComSucesso() {
        long id = 1L;
        Artista artista = new Artista(1L, "Artista teste atualizado");
        Mockito.when(repository.save(artista)).thenReturn(artista);
        Assertions.assertEquals(artista, service.update(artista, id));
    }

    @Test
    void testDeletarArtistaComSucesso() {
        long id = 1L;
        Assertions.assertAll(() -> service.delete(id));
    }

    @Test
    void testBuscarArtistaComSucesso() {
        Assertions.assertAll(() -> service.findAll());
    }

    @Test
    void testBuscarArtistaPorAlbumIdComSucesso() {
        long albumId = 1L;
        Mockito.when(albumArtistaRepository.findByAlbumId(albumId)).thenReturn(List.of(new AlbumArtista(1L, 1L)));
        Assertions.assertAll(() -> service.findByAlbumId(albumId));
    }

    @Test
    void testBuscarArtistaPorIddComSucesso() {
        long id = 1L;
        Artista artista = new Artista(1L, "Artista teste");
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(artista));
        Assertions.assertEquals(artista, service.findById(id));
    }



    @Test
    void testBuscarArtistaComErroNenhumArtistaEncontrado() {
        long id = 1L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById(id));
    }
}

 */
