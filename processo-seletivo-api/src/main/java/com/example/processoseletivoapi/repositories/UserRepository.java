package com.example.processoseletivoapi.repositories;

import com.example.processoseletivoapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u.* " +
            "from tb_users u " +
            "where :roleId = any(u.id_roles) ",
    nativeQuery = true)
    List<User> findByRoleId(Long roleId);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE UPPER(u.username) = UPPER(:username) ")
    Optional<User> findByUsername(String username);
}
