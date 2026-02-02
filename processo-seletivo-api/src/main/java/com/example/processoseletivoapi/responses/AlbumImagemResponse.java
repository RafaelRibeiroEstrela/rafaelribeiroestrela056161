package com.example.processoseletivoapi.responses;

public record AlbumImagemResponse(
        Long id,
        Long albumId,
        String fileName,
        String fileContentType,
        String fileHash,
        String fileContent,
        String storageKey,
        String linkPreAssinado
) {


}
