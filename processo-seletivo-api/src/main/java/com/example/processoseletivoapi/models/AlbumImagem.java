package com.example.processoseletivoapi.models;

import com.example.processoseletivoapi.exceptions.BusinessException;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_albuns_imagens")
public class AlbumImagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_album")
    private Long albumId;
    private String fileName;
    private String fileContentType;
    private String fileHash;
    private String storageKey;
    @Transient
    private byte[] content;

    public AlbumImagem() {
    }

    public AlbumImagem(Long albumId, String fileName, String fileContentType, byte[] content) {
        this.setAlbumId(albumId);
        this.setFileName(fileName);
        this.setFileContentType(fileContentType);
        this.setContent(content);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        if (albumId == null) {
            throw new BusinessException("O identificador do album é obrigatório");
        }
        this.albumId = albumId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new BusinessException("O nome do arquivo é obrigatório");
        }
        this.fileName = fileName;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        if (fileContentType == null || fileContentType.trim().isEmpty()) {
            throw new BusinessException("O content type do arquivo é obrigatório");
        }
        this.fileContentType = fileContentType;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        if (fileHash == null || fileHash.trim().isEmpty()) {
            throw new BusinessException("O hash do arquivo é obrigatório");
        }
        this.fileHash = fileHash;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        if (storageKey == null || storageKey.trim().isEmpty()) {
            throw new BusinessException("A chave do arquivo é obrigatório");
        }
        this.storageKey = storageKey;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        if (content == null) {
            throw new BusinessException("O content do arquivo é obrigatório");
        }
        this.content = content;
    }
}
