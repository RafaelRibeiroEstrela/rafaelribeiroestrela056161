package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.BusinessException;
import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.models.Album;
import com.example.processoseletivoapi.repositories.AlbumArtistaRepository;
import com.example.processoseletivoapi.repositories.AlbumImagemRepository;
import com.example.processoseletivoapi.repositories.AlbumRepository;
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
class AlbumServiceTest {

    @InjectMocks
    private AlbumService service;

    @Mock
    private AlbumRepository repository;

    @Mock
    private AlbumArtistaRepository albumArtistaRepository;

    @Mock
    private AlbumImagemRepository albumImagemRepository;


    @Test
    void testCriarAlbumComSucesso() {
        Assertions.assertAll(() -> new Album(null, "Album teste"));
        Album albumNovo = new Album(null, "Album teste");
        Album albumSalvo = new Album(1L, "Album teste");
        Set<Long> artistaIdList = Set.of(1L);
        Mockito.when(repository.save(albumNovo)).thenReturn(albumSalvo);
        Assertions.assertEquals(albumSalvo, service.create(albumNovo, artistaIdList));
    }

    @Test
    void testCriarAlbumComFalhaCampoObrigatorio() {
        Assertions.assertThrows(BusinessException.class, () -> new Album(null, null));
        Assertions.assertThrows(BusinessException.class, () -> new Album(null, ""));
    }

    @Test
    void testAtualizarAlbumComSucesso() {
        Assertions.assertAll(() -> new Album(1L, "Album teste"));
        Album album = new Album(1L, "Album teste");
        Album albumSalvo = new Album(1L, "Album teste");
        long id = 1L;
        Set<Long> artistaIdList = Set.of(1L);
        Mockito.when(repository.save(album)).thenReturn(albumSalvo);
        Assertions.assertEquals(albumSalvo, service.update(album, id, artistaIdList));
    }

    @Test
    void testAtualizarAlbumComSucessoQuandoNaoHaArtistas() {
        Assertions.assertAll(() -> new Album(1L, "Album teste"));
        Album album = new Album(1L, "Album teste");
        Album albumSalvo = new Album(1L, "Album teste");
        long id = 1L;
        Set<Long> artistaIdList = Set.of();
        Mockito.when(repository.save(album)).thenReturn(albumSalvo);
        Assertions.assertEquals(albumSalvo, service.update(album, id, artistaIdList));
    }

    @Test
    void testAtualizarAlbumComFalhaRepetindoMesmoArtistaParaAlbum() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Set.of(1L, 1L));
    }


    @Test
    void testBuscarAlbumPorIdComSucesso() {
        long id = 1L;
        Album album = new Album(1L, "Album teste");
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(album));
        Assertions.assertEquals(album, service.findById(id));
    }

    @Test
    void testDeletarAlbumPorIdComSucesso() {
        long id = 1L;
        Assertions.assertAll(() -> service.delete(id));
    }

    @Test
    void testBuscarAlbumPorParametrosComSucesso() {
        Boolean possuiCantores = null;
        String nomeArtista = null;
        Pageable pageable = PageRequest.of(0, 10);
        Assertions.assertAll(() -> service.find(possuiCantores, nomeArtista, pageable));
    }

    @Test
    void testBuscarAlbumPorIdComFalhaNenhumAlbumEncontrado() {
        long id = 1L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById(id));
    }
}
