package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.models.AlbumImagem;
import com.example.processoseletivoapi.repositories.AlbumImagemRepository;
import com.example.processoseletivoapi.storages.StorageClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AlbumImagemService {

    private final AlbumImagemRepository repository;
    private final StorageClient storageClient;

    public AlbumImagemService(AlbumImagemRepository repository, StorageClient storageClient) {
        this.repository = repository;
        this.storageClient = storageClient;
    }

    public AlbumImagem upload(AlbumImagem model) {
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

    public List<AlbumImagem> downloadByAlbumId(Long albumId) {
        List<AlbumImagem> models = repository.findByAlbumId(albumId);
        models.forEach(obj -> {
            byte[] content = storageClient.download(obj.getStorageKey());
            obj.setContent(content);
        });
        return models;
    }
}
