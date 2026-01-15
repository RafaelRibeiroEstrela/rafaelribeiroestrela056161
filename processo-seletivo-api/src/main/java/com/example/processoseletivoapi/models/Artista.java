package com.example.processoseletivoapi.models;

import com.example.processoseletivoapi.exceptions.BusinessException;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_artistas")
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    public Artista() {
    }

    public Artista(Long id, String nome) {
        this.setId(id);
        this.setNome(nome);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new BusinessException("O nome é obrigatório");
        }
        this.nome = nome;
    }
}
