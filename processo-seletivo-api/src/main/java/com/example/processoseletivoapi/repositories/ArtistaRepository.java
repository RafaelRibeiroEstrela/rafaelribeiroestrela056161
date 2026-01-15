package com.example.processoseletivoapi.repositories;

import com.example.processoseletivoapi.models.Artista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {
}
