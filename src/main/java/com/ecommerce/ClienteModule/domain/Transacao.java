package com.ecommerce.ClienteModule.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@Entity
public class Transacao extends EntidadeDominio {
    private LocalDateTime dataHora;
    private Double valor;
    private String status;

    @ManyToOne
    private Cliente cliente;
    private String usuario;

}
