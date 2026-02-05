package com.example.processoseletivoapi.mappers;

import com.example.processoseletivoapi.exceptions.StorageException;
import com.example.processoseletivoapi.models.AlbumImagem;
import com.example.processoseletivoapi.responses.AlbumImagemResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Component
public class AlbumImagemMapper {

    public AlbumImagem multipartFileToModel(MultipartFile file, Long albumId) {
        if (file == null) return null;
        try {
            return new AlbumImagem(albumId, file.getOriginalFilename(), file.getContentType(), file.getBytes());
        } catch (IOException e) {
            throw new StorageException("Erro ao obter arquivo: " + e.getMessage());
        }
    }

    public AlbumImagemResponse modelToResponse(AlbumImagem model) {
        if (model == null) return null;
        String contentBase64 = model.getContent() == null ? null : Base64.getEncoder().encodeToString(model.getContent());
        return new AlbumImagemResponse(model.getId(), model.getAlbumId(), model.getFileName(), model.getFileContentType(), model.getFileHash(), model.getStorageKey(), model.getLinkPreAssinado(), contentBase64);
    }


}
