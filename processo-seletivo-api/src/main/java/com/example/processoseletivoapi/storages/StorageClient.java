package com.example.processoseletivoapi.storages;

public interface StorageClient {

    void upload(byte[] conteudo, String chave);

    byte[] download(String chave);

    void delete(String chave);
}
