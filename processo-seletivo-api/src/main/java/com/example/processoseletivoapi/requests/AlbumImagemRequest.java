package com.example.processoseletivoapi.requests;

public record AlbumImagemRequest(
        String fileName,
        String fileContentType,
        String fileContent
) {

}
