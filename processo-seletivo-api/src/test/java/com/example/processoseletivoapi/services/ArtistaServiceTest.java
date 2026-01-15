package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.BusinessException;
import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.models.Artista;
import com.example.processoseletivoapi.repositories.ArtistaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ArtistaServiceTest {

    @InjectMocks
    private ArtistaService service;

    @Mock
    private ArtistaRepository repository;

    @Test
    void criarArtistaComSucesso() {
        Assertions.assertAll(() -> new Artista(null, "Artista teste"));
        Artista artista = new Artista(1L, "Artista teste");
        Assertions.assertAll(() -> service.create(artista));
    }

    @Test
    void criarArtistaComErroCampoObrigatorio() {
        Assertions.assertThrows(BusinessException.class, () -> new Artista(null, ""));
        Assertions.assertThrows(BusinessException.class, () -> new Artista(null, null));
    }

    @Test
    void buscarArtistaComSucesso() {
        long id = 1L;
        Artista artista = new Artista(1L, "Artista teste");
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(artista));
        Assertions.assertEquals(artista, service.findById(id));
    }

    @Test
    void buscarArtistaComErroNenhumArtistaEncontrado() {
        long id = 1L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById(id));
    }
}
