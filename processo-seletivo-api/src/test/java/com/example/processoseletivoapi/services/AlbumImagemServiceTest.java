package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.BusinessException;
import com.example.processoseletivoapi.exceptions.StorageException;
import com.example.processoseletivoapi.models.AlbumImagem;
import com.example.processoseletivoapi.repositories.AlbumImagemRepository;
import com.example.processoseletivoapi.storages.StorageClient;
import org.apache.tika.Tika;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AlbumImagemServiceTest {

    @InjectMocks
    private AlbumImagemService service;

    @Mock
    private AlbumImagemRepository repository;

    @Mock
    private StorageClient storageClient;

    @Mock
    private Tika tika;

    @Mock
    private TokenService tokenService;

    @Test
    void testUploadComSucesso() {
        Long albumId = 1L;
        String fileName = "capa-album.png";
        String contentType = "image/png";
        byte[] content = "imagem".getBytes();
        AlbumImagem albumImagem = new AlbumImagem(albumId, fileName, contentType, content);
        Assertions.assertAll(() -> new AlbumImagem(albumId, fileName, contentType, content));
        Mockito.when(tika.detect(content)).thenReturn("image/png");
        Assertions.assertAll(() -> service.upload(albumImagem));
    }

    @Test
    void testUploadComFalhaArquivoNaoEImagem() {
        Long albumId = 1L;
        String fileName = "capa-album.png";
        String contentType = "image/png";
        byte[] content = "imagem".getBytes();
        AlbumImagem albumImagem = new AlbumImagem(albumId, fileName, contentType, content);
        Mockito.when(tika.detect(content)).thenReturn("application/json");
        Assertions.assertThrows(BusinessException.class, () -> service.upload(albumImagem));
    }

    @Test
    void testDownloadLinkPreAssinadoComSucesso() {
        String token = "token";
        String key = "key";
        Mockito.when(tokenService.isTokenPreAssinadoValido(token)).thenReturn(true);
        Mockito.when(tokenService.extractUsername(token)).thenReturn(key);
        Long albumId = 1L;
        String fileName = "capa-album.png";
        String contentType = "image/png";
        byte[] content = "imagem".getBytes();
        AlbumImagem albumImagem = new AlbumImagem(albumId, fileName, contentType, content);
        Mockito.when(repository.findByStorageKey(key)).thenReturn(Optional.of(albumImagem));
        Mockito.when(storageClient.download(key)).thenReturn(content);
        Assertions.assertEquals(albumImagem, service.downloadLinkPreAssinado(token));
    }

    @Test
    void testDownloadLinkPreAssinadoComFalhaTokenExpirado() {
        String token = "token";
        Assertions.assertThrows(StorageException.class, () -> service.downloadLinkPreAssinado(token));
    }

    @Test
    void testDownloadByStorageKey() {
        String storageKey = "storageKey";
        Long albumId = 1L;
        String fileName = "capa-album.png";
        String contentType = "image/png";
        byte[] content = "imagem".getBytes();
        AlbumImagem albumImagem = new AlbumImagem(albumId, fileName, contentType, content);
        Mockito.when(repository.findByStorageKey(storageKey)).thenReturn(Optional.of(albumImagem));
        Mockito.when(storageClient.download(storageKey)).thenReturn(content);
        Assertions.assertEquals(albumImagem, service.downloadByStorageKey(storageKey));
    }

    @Test
    void testDownloadByAlbumIdComFalhaNenhumArquivoEncontrado() {
        Long albumId = 1L;
        Mockito.when(repository.findByAlbumId(albumId)).thenReturn(List.of());
        Assertions.assertThrows(BusinessException.class, () -> service.downloadByAlbumId(albumId));
    }

    @Test
    void testDownloadByAlbumId() {
        Long albumId = 1L;
        String fileName = "capa-album.png";
        String contentType = "image/png";
        byte[] content = "imagem".getBytes();
        String storageKey = "storageKey";
        AlbumImagem albumImagem = new AlbumImagem(albumId, fileName, contentType, content);
        albumImagem.setStorageKey(storageKey);
        Mockito.when(repository.findByAlbumId(albumId)).thenReturn(List.of(albumImagem));
        Mockito.when(storageClient.download(storageKey)).thenReturn(content);
        Assertions.assertFalse(service.downloadByAlbumId(albumId).isEmpty());
    }

    @Test
    void testRecoveryMetadata() {
        Assertions.assertAll(() -> service.recoverMetadataByAlbumId(1L));
    }

    @Test
    void testDeleteByAlbumId() {
        Long albumId = 1L;
        String fileName = "capa-album.png";
        String contentType = "image/png";
        byte[] content = "imagem".getBytes();
        String storageKey = "storageKey";
        AlbumImagem albumImagem = new AlbumImagem(albumId, fileName, contentType, content);
        albumImagem.setStorageKey(storageKey);
        Mockito.when(repository.findByAlbumId(albumId)).thenReturn(List.of(albumImagem));
        Assertions.assertAll(() -> service.deleteByAlbumId(albumId));
    }

    @Test
    void testDeleteByStorageKey() {
        Assertions.assertAll(() -> service.deleteByStorageKey("storageKey"));
    }
}
