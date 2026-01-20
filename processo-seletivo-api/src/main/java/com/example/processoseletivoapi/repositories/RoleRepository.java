package com.example.processoseletivoapi.repositories;

import com.example.processoseletivoapi.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByAuthority(String authority);
}
