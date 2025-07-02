package com.ecommerce.ClienteModule.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class TipoTelefone extends EntidadeDominio {
    private String descricao;

    public TipoTelefone() {}

    public TipoTelefone(String descricao) {
        this.descricao = descricao;
    }

}
