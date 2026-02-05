package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.BusinessException;
import com.example.processoseletivoapi.exceptions.ResourceNotFoundException;
import com.example.processoseletivoapi.models.User;
import com.example.processoseletivoapi.repositories.RoleRepository;
import com.example.processoseletivoapi.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User model) {
        if (repository.findByUsername(model.getUsername()).isPresent()) {
            throw new BusinessException("Já existe um usuário com username " + model.getUsername());
        }
        model.setPassword(passwordEncoder.encode(model.getPassword()));
        return repository.save(model);
    }

    public void updateRoles(Set<Long> rolesId, String username) {
        User model = repository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Nenhum usuário encontrado"));
        for (Long roleId : rolesId) {
            if (!roleRepository.existsById(roleId)) {
                throw new BusinessException("Nenhum role encontrado com id = " + roleId);
            }
        }
        model.setRoles(rolesId.toArray(new Long[0]));
        repository.save(model);
    }

    public void updatePassword(String oldPassword, String newPassword, String username) {
        User model = repository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("O login/senha são inválidos"));
        if (!passwordEncoder.matches(oldPassword, model.getPassword())) {
            throw new BusinessException("O login/senha são inválidos");
        }
        model.setPassword(passwordEncoder.encode(newPassword));
        repository.save(model);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Nenhum usuário encontrado"));
    }
}
