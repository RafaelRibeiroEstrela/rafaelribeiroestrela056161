package com.example.processoseletivoapi.repositories;

import com.example.processoseletivoapi.models.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
}
