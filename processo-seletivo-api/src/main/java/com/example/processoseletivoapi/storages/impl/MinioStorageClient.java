package com.example.processoseletivoapi.storages.impl;

import com.example.processoseletivoapi.exceptions.StorageException;
import com.example.processoseletivoapi.storages.StorageClient;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class MinioStorageClient implements StorageClient {

    @Value("${minio.bucket}")
    private String bucket;

    private final MinioClient client;

    public MinioStorageClient(MinioClient client) {
        this.client = client;
    }

    @Override
    public void upload(byte[] conteudo, String chave) {
        try {
            client.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(chave)
                            .stream(new ByteArrayInputStream(conteudo), conteudo.length, -1)
                            .build()
            );
        } catch (Exception e) {
            throw new StorageException("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    @Override
    public byte[] download(String chave) {
        try {
            return client.getObject(
                            GetObjectArgs.builder()
                                    .bucket(bucket)
                                    .object(chave)
                                    .build())
                    .readAllBytes();
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new StorageException("Erro ao ler arquivo: " + e.getMessage());
        }
    }

    @Override
    public void delete(String chave) {
        try {
            client.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(chave)
                    .build());
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new StorageException("Erro ao deletar arquivo: " + e.getMessage());
        }
    }
}
