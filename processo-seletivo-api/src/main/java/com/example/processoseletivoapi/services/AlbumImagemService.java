package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.BusinessException;
import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.models.AlbumImagem;
import com.example.processoseletivoapi.repositories.AlbumImagemRepository;
import com.example.processoseletivoapi.storages.StorageClient;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AlbumImagemService {

    private final AlbumImagemRepository repository;
    private final StorageClient storageClient;
    private final Tika tika;

    public AlbumImagemService(AlbumImagemRepository repository, StorageClient storageClient, Tika tika) {
        this.repository = repository;
        this.storageClient = storageClient;
        this.tika = tika;
    }

    public AlbumImagem upload(AlbumImagem model) {
        if (!isImagem(model.getContent())) {
            throw new BusinessException("O arquivo deve ser uma imagem");
        }
        String hash = UUID.randomUUID().toString();
        String key = "/albuns/" + model.getAlbumId() + "/" + hash + "/" + model.getFileName();
        model.setFileHash(hash);
        model.setStorageKey(key);
        repository.save(model);
        storageClient.upload(model.getContent(), model.getStorageKey());
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
        models.forEach(obj -> {
            storageClient.delete(obj.getStorageKey());
        });
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
}
