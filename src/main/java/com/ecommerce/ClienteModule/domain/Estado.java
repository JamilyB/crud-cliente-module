package com.ecommerce.ClienteModule.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Estado extends EntidadeDominio {
    private String descricao;
    private String uf;

    public Estado() {}

    public Estado(String descricao, String uf) {
        this.descricao = descricao;
        this.uf = uf;

    }

}

