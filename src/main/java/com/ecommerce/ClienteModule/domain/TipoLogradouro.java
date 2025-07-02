package com.ecommerce.ClienteModule.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class TipoLogradouro extends EntidadeDominio {
    private String descricao;

    public TipoLogradouro() {}

    public TipoLogradouro(String descricao) {
        this.descricao = descricao;
    }

}

