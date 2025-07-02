package com.ecommerce.ClienteModule.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Cidade extends EntidadeDominio {
    private String descricao;
    @ManyToOne
    private Estado estado;

    public Cidade() {}

    public Cidade(String descricao, Estado estado) {
        this.descricao = descricao;
        this.estado = estado;
    }

}

