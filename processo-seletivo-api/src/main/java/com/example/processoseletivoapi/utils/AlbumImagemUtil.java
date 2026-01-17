package com.example.processoseletivoapi.utils;

import com.example.processoseletivoapi.exceptions.StorageException;
import com.example.processoseletivoapi.models.AlbumImagem;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class AlbumImagemUtil {

    public byte[] compactFilesToZip(List<AlbumImagem> files) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
                for (AlbumImagem file : files) {
                    if (file.getContent() != null) {
                        ZipEntry zipEntry = new ZipEntry(file.getFileName());
                        zipOutputStream.putNextEntry(zipEntry);
                        zipOutputStream.write(file.getContent());
                        zipOutputStream.closeEntry();
                    }
                }
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new StorageException("Falha ao converter arquivos para zip: " + e.getMessage());
        }
    }
}
