package com.ecommerce.ClienteModule.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class DDD extends EntidadeDominio {
    private String numero;

    public DDD() {}

    public DDD(String numero) {
        this.numero = numero;
    }

}
