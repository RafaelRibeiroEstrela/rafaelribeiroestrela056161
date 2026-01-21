package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.models.RateLimit;
import com.example.processoseletivoapi.repositories.RateLimitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RateLimitService {

    private final RateLimitRepository repository;

    public RateLimitService(RateLimitRepository repository) {
        this.repository = repository;
    }

    public void registrarAcesso(String username) {
        Optional<RateLimit> optional = repository.findById(username);
        if (optional.isEmpty()) {
            repository.save(new RateLimit(username, LocalDateTime.now()));
        } else {
            RateLimit rateLimit = optional.get();
            rateLimit.getAcessos().add(LocalDateTime.now());
            repository.save(rateLimit);
        }
    }

    public boolean verificarSeAcessoPermitido(String username) {
        Optional<RateLimit> optional = repository.findById(username);
        if (optional.isEmpty()) {
            return true;
        }
        RateLimit rateLimit = optional.get();
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime minusMinutes = agora.minusMinutes(1L);
        long count = rateLimit.getAcessos().stream().filter(obj -> !obj.isBefore(minusMinutes)).count();
        return count <= 10;
    }
}
