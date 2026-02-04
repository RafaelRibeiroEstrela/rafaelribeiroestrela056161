package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.clients.RegionalClient;
import com.example.processoseletivoapi.mappers.RegionalMapper;
import com.example.processoseletivoapi.models.Regional;
import com.example.processoseletivoapi.repositories.RegionalRepository;
import com.example.processoseletivoapi.responses.RegionalResponse;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class RegionalServiceTest {

    @InjectMocks
    private RegionalService service;

    @Mock
    private RegionalRepository repository;

    @Mock
    private RegionalClient client;

    @Mock
    private RegionalMapper mapper;

    @Test
    void testSincronizarComSucesso() {
        RegionalResponse response = new RegionalResponse(1L, "Regional teste ", null);
        Regional model = new Regional(null, 1L, "RegionalTeste", null);
        List<RegionalResponse> reginais = List.of(response);
        Mockito.when(client.findAll()).thenReturn(reginais);
        Mockito.when(repository.findAllLocked()).thenReturn(List.of());
        Mockito.when(mapper.responseToModel(response)).thenReturn(model);
        Assertions.assertAll(() -> service.sync());
    }

    @Test
    void testSincronizarComSucessoAtualizandoDados() {
        RegionalResponse response = new RegionalResponse(1L, "Regional teste ", null);
        Regional model = new Regional(null, 1L, "RegionalTeste", null);
        Regional modelSaved = new Regional(1L, 1L, "RegionalTesteX", true);
        List<RegionalResponse> reginais = List.of(response);
        Mockito.when(client.findAll()).thenReturn(reginais);
        Mockito.when(repository.findAllLocked()).thenReturn(List.of(modelSaved));
        Mockito.when(mapper.responseToModel(response)).thenReturn(model);
        Assertions.assertAll(() -> service.sync());
    }

    @Test
    void testBuscarComSucesso() {
        Assertions.assertAll(() -> service.findAll());
    }

    @Test
    void testSincronizarComFalhaApiIndisponivel() {
        FeignException feignException = Mockito.mock(FeignException.class);
        Mockito.when(client.findAll()).thenThrow(feignException);
        Assertions.assertThrows(FeignException.class, () -> service.sync());
    }

}
