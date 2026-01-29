package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.BusinessException;
import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.exceptions.StorageException;
import com.example.processoseletivoapi.models.AlbumImagem;
import com.example.processoseletivoapi.repositories.AlbumImagemRepository;
import com.example.processoseletivoapi.storages.StorageClient;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AlbumImagemService {

    private final AlbumImagemRepository repository;
    private final StorageClient storageClient;
    private final Tika tika;
    private final TokenService tokenService;

    @Value("${server.port}")
    private String applicationPort;

    public AlbumImagemService(AlbumImagemRepository repository, StorageClient storageClient, Tika tika, TokenService tokenService) {
        this.repository = repository;
        this.storageClient = storageClient;
        this.tika = tika;
        this.tokenService = tokenService;
    }

    public AlbumImagem upload(AlbumImagem model) {
        if (!isImagem(model.getContent())) {
            throw new BusinessException("O arquivo deve ser uma imagem");
        }
        String hash = UUID.randomUUID().toString();
        String key = "/albuns/" + model.getAlbumId() + "/" + hash + "/" + model.getFileName();
        model.setFileHash(hash);
        model.setStorageKey(key);
        model.setCreatedAt(LocalDateTime.now());
        model.setLinkPreAssinado(generateUrl(key));
        repository.save(model);
        storageClient.upload(model.getContent(), model.getStorageKey());
        return model;
    }

    public AlbumImagem downloadLinkPreAssinado(String token) {
        if (!tokenService.isTokenPreAssinadoValido(token)) {
            throw new StorageException("Tempo expirado para recuperar o arquivo");
        }
        String key = tokenService.extractUsername(token);
        AlbumImagem model = repository.findByStorageKey(key).orElseThrow(() -> new ResourceNotFoundException("Nenhum arquivo encontrado"));
        byte[] content = storageClient.download(key);
        model.setContent(content);
        return model;
    }

    public AlbumImagem downloadByStorageKey(String storageKey) {
        AlbumImagem model = repository.findByStorageKey(storageKey).orElseThrow(() -> new ResourceNotFoundException("Nenhum arquivo encontrado"));
        byte[] content = storageClient.download(storageKey);
        model.setContent(content);
        return model;
    }

    public List<AlbumImagem> downloadByAlbumId(long albumId) {
        List<AlbumImagem> models = repository.findByAlbumId(albumId);
        if (models.isEmpty()) {
            throw new BusinessException("Nenhum arquivo encontrado");
        }
        models.forEach(obj -> {
            byte[] content = storageClient.download(obj.getStorageKey());
            obj.setContent(content);
        });
        return models;
    }

    public List<AlbumImagem> recoverMetadataByAlbumId(long albumId) {
        return repository.findByAlbumId(albumId);
    }

    public void deleteByAlbumId(long albumId) {
        List<AlbumImagem> models = repository.findByAlbumId(albumId);
        models.forEach(obj -> storageClient.delete(obj.getStorageKey()));
        repository.deleteByAlbumId(albumId);
    }

    public void deleteByStorageKey(String key) {
        storageClient.delete(key);
        repository.deleteByStorageKey(key);
    }

    private boolean isImagem(byte[] content) {
        try {
            String mimeType = tika.detect(content);
            return mimeType != null && mimeType.startsWith("image/");
        } catch (Exception e) {
            return false;
        }
    }

    private String generateUrl(String key) {
        String token = tokenService.generateTokenLinkPreAssinado(key);
        return "http://localhost:" + applicationPort + "/minio/miniobucket/" + token;
    }
}
