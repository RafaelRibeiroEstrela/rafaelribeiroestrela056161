package com.example.processoseletivoapi.models;

import com.example.processoseletivoapi.exceptions.BusinessException;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Column(name = "id_roles", columnDefinition = "bigint[]")
    private Long[] roles;

    public User() {
    }

    public User(String username, String password, Long[] roles) {
        this.setUsername(username);
        this.setPassword(password);
        this.setRoles(roles);
    }

    public User(Long id, String username, String password, Long[] roles) {
        this.setId(id);
        this.setUsername(username);
        this.setPassword(password);
        this.setRoles(roles);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException("Username is required");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException("Password is required");
        }
        this.password = password;
    }

    public Long[] getRoles() {
        return roles;
    }

    public void setRoles(Long[] roles) {
        if (roles == null || roles.length == 0) {
            throw new BusinessException("Role(s) is required");
        }
        this.roles = roles;
    }
}
