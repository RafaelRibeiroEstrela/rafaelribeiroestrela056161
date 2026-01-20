package com.example.processoseletivoapi.repositories;

import com.example.processoseletivoapi.models.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, String> {

    Optional<Token> findByToken(String token);
}
