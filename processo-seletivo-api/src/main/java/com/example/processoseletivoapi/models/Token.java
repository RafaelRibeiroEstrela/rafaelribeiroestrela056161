package com.example.processoseletivoapi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash
public class Token {

    @Id
    private String token;
    private LocalDateTime deletedAt;

    public Token() {
    }

    public Token(String token, LocalDateTime deletedAt) {
        this.token = token;
        this.deletedAt = deletedAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
