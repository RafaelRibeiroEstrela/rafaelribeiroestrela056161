package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.BusinessException;
import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.models.Artista;
import com.example.processoseletivoapi.repositories.AlbumArtistaRepository;
import com.example.processoseletivoapi.repositories.AlbumRepository;
import com.example.processoseletivoapi.repositories.ArtistaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;


@ExtendWith(MockitoExtension.class)
class ArtistaServiceTest {

    @InjectMocks
    private ArtistaService service;

    @Mock
    private ArtistaRepository repository;

    @Mock
    private AlbumArtistaRepository albumArtistaRepository;

    @Mock
    private AlbumRepository albumRepository;

    @Test
    void testCriarArtistaComSucesso() {
        Assertions.assertAll(() -> new Artista(null, "Artista teste"));
        Artista artista = new Artista(1L, "Artista teste");
        Set<Long> listaAlbumId = Set.of(1L);
        Mockito.when(albumRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(repository.save(artista)).thenReturn(artista);
        Assertions.assertEquals(artista, service.create(artista, listaAlbumId));
    }

    @Test
    void testCriarArtistaComSucessoComNenhumAlbum() {
        Assertions.assertAll(() -> new Artista(null, "Artista teste"));
        Artista artista = new Artista(1L, "Artista teste");
        Set<Long> listaAlbumId = Set.of();
        Mockito.when(repository.save(artista)).thenReturn(artista);
        Assertions.assertEquals(artista, service.create(artista, listaAlbumId));
    }

    @Test
    void testCriarArtistaFalhaNenhumAlbumEncontrado() {
        Artista artista = new Artista(1L, "Artista teste");
        Set<Long> listaAlbumId = Set.of(1L);
        Mockito.when(albumRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(repository.save(artista)).thenReturn(artista);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.create(artista, listaAlbumId));
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
        Set<Long> listaAlbumId = Set.of(1L);
        Mockito.when(repository.save(artista)).thenReturn(artista);
        Mockito.when(albumRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Assertions.assertEquals(artista, service.update(artista, listaAlbumId, id));
    }

    @Test
    void testAtualizarArtistaComSucessoComNenhumAlbum() {
        long id = 1L;
        Artista artista = new Artista(1L, "Artista teste atualizado");
        Set<Long> listaAlbumId = Set.of();
        Mockito.when(repository.save(artista)).thenReturn(artista);
        Assertions.assertEquals(artista, service.update(artista, listaAlbumId, id));
    }

    @Test
    void testAtualizarArtistaComFalhaNenhumAlbumEncontrado() {
        long id = 1L;
        Artista artista = new Artista(1L, "Artista teste atualizado");
        Set<Long> listaAlbumId = Set.of(1L);
        Mockito.when(repository.save(artista)).thenReturn(artista);
        Mockito.when(albumRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(artista, listaAlbumId, id));
    }

    @Test
    void testDeletarArtistaComSucesso() {
        long id = 1L;
        Assertions.assertAll(() -> service.delete(id));
    }


    @Test
    void testBuscarArtistaComSucesso() {
        String nomeArtista = "Zé xicará";
        Pageable pageable = PageRequest.of(0, 10);
        Assertions.assertAll(() -> service.find(nomeArtista, pageable));
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

