package com.example.processoseletivoapi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash(timeToLive = 86400L) //24h
public class Token {

    @Id
    private String id;
    private LocalDateTime deletedAt;

    public Token() {
    }

    public Token(String id, LocalDateTime deletedAt) {
        this.id = id;
        this.deletedAt = deletedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
