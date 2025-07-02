package com.ecommerce.ClienteModule.domain;


import jakarta.persistence.Entity;

@Entity
public class Bandeira extends EntidadeDominio {
    private String descricao;

    public Bandeira() {}

    public Bandeira(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

