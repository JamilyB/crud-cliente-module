package com.ecommerce.ClienteModule.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class TipoResidencial extends EntidadeDominio {
    private String descricao;

    public TipoResidencial() {}

    public TipoResidencial(String descricao) {
        this.descricao = descricao;
    }

}

