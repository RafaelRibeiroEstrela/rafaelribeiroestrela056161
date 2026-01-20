package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.BusinessException;
import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.models.Role;
import com.example.processoseletivoapi.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public Role create(Role model) {
        if (repository.existsByAuthority(model.getAuthority())) {
            throw new BusinessException("Role " + model.getAuthority() + " already exists");
        }
        return repository.save(model);
    }

    public Role update(Role model, long id) {
        Role modelSaved = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nenhum perfil encontrado"));
        if (!model.getAuthority().equals(modelSaved.getAuthority())) {
            if (repository.existsByAuthority(model.getAuthority())) {
                throw new BusinessException("Role " + model.getAuthority() + " already exists");
            }
        }
        model.setId(id);
        return repository.save(model);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public List<Role> findAll() {
        return repository.findAll();
    }
}
