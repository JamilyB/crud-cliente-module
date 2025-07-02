package com.ecommerce.ClienteModule.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Cartao extends EntidadeDominio {

    private String numCartao;
    private String nomeImpresso;
    private String codSeguranca;

    @ManyToOne
    private Bandeira bandeira;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Cartao() {}

    public Cartao(String numCartao, String nomeImpresso, String codSeguranca, Bandeira bandeira, Cliente cliente) {
        this.numCartao = numCartao;
        this.nomeImpresso = nomeImpresso;
        this.codSeguranca = codSeguranca;
        this.bandeira = bandeira;
        this.cliente = cliente;
    }

}
