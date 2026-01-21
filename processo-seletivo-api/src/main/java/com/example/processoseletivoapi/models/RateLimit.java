package com.example.processoseletivoapi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RedisHash
public class RateLimit {

    @Id
    private String username;
    private List<LocalDateTime> acessos = new ArrayList<>();

    public RateLimit() {}

    public RateLimit(String username, LocalDateTime acesso) {
        this.username = username;
        this.acessos.add(acesso);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<LocalDateTime> getAcessos() {
        return acessos;
    }

    public void setAcessos(List<LocalDateTime> acessos) {
        this.acessos = acessos;
    }
}
