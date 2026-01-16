package com.example.processoseletivoapi.repositories;

import com.example.processoseletivoapi.models.Regional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionalRepository extends JpaRepository<Regional, Long> {

    Optional<Regional> findByRegionalId(Long regionalId);
}
