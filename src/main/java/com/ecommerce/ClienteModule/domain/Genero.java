package com.ecommerce.ClienteModule.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Genero extends EntidadeDominio {
    private String descricao;

    public Genero() {}

    public Genero(String descricao) {
        this.descricao = descricao;
    }

    public Genero(Long generoId) {
    }

}
