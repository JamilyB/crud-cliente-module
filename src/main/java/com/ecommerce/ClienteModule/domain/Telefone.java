package com.ecommerce.ClienteModule.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;


@Entity
public class Telefone extends EntidadeDominio {
    @ManyToOne
    private TipoTelefone tipoTelefone;
    @ManyToOne
    private DDD ddd;
    private String numero;

    @OneToOne
    private Cliente cliente;

    public Telefone() {}

    public Telefone(TipoTelefone tipoTelefone, DDD ddd, String numero) {
        this.tipoTelefone = tipoTelefone;
        this.ddd = ddd;
        this.numero = numero;

    }

    public TipoTelefone getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(TipoTelefone tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    public DDD getDdd() {
        return ddd;
    }

    public void setDdd(DDD ddd) {
        this.ddd = ddd;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}

