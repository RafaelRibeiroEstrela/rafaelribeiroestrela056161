package com.example.processoseletivoapi.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_regionais")
public class Regional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_externo")
    private Long regionalId;
    private String nome;
    private boolean ativo;

    public Regional() {}

    public Regional(Long id, Long regionalId, String nome, boolean ativo) {
        this.setId(id);
        this.setRegionalId(regionalId);
        this.setNome(nome);
        this.setAtivo(ativo);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRegionalId() {
        return regionalId;
    }

    public void setRegionalId(Long regionalId) {
        this.regionalId = regionalId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Regional regional = (Regional) o;
        return Objects.equals(regionalId, regional.regionalId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(regionalId);
    }
}
