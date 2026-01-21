package com.example.processoseletivoapi.repositories;

import com.example.processoseletivoapi.models.RateLimit;
import org.springframework.data.repository.CrudRepository;

public interface RateLimitRepository extends CrudRepository<RateLimit, String> {

}
