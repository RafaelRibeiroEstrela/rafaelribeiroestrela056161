package com.example.processoseletivoapi.models;

import com.example.processoseletivoapi.exceptions.BusinessException;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "tb_roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;

    public Role() {
    }

    public Role(String authority) {
        this.setAuthority(authority);
    }

    public Role(Long id, String authority) {
        this.setId(id);
        this.setAuthority(authority);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        if (authority == null) {
            throw new BusinessException("Authority is required");
        }
        if (!authority.startsWith("ROLE_")) {
            authority = "ROLE_" + authority;
        }
        authority = authority.trim().toUpperCase();
        this.authority = authority;
    }
}
