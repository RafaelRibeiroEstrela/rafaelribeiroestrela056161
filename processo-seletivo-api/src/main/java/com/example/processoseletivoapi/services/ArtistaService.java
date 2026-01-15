package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.models.Artista;
import com.example.processoseletivoapi.repositories.ArtistaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistaService {

    private final ArtistaRepository repository;

    public ArtistaService(ArtistaRepository repository) {
        this.repository = repository;
    }

    public Artista create(Artista model) {
        return repository.save(model);
    }

    public Artista update(Artista model, long id) {
        model.setId(id);
        return repository.save(model);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public List<Artista> findAll() {
        return repository.findAll();
    }

    public Artista findById(long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nenhum artista encontrado"));
    }
}
