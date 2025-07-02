package com.ecommerce.ClienteModule.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Endereco extends EntidadeDominio {

    private String tipo;

    @ManyToOne
    @JoinColumn(name = "tipo_residencial_id")
    private TipoResidencial tipoResidencial;

    @ManyToOne
    @JoinColumn(name = "tipo_logradouro_id")
    private TipoLogradouro tipoLogradouro;

    private String logradouro;
    private int numero;
    private String bairro;
    private String cep;

    @ManyToOne
    @JoinColumn(name = "cidade_id")
    private Cidade cidade;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Endereco() {}

    public Endereco(String tipo, TipoResidencial tipoResidencial, TipoLogradouro tipoLogradouro, String logradouro,
                    int numero, String bairro, String cep, Cidade cidade, Cliente cliente) {
        this.tipoResidencial = tipoResidencial;
        this.tipoLogradouro = tipoLogradouro;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cep = cep;
        this.cidade = cidade;
        this.cliente = cliente;
        this.tipo = tipo;
    }

    public TipoResidencial getTipoResidencial() {
        return tipoResidencial;
    }

    public void setTipoResidencial(TipoResidencial tipoResidencial) {
        this.tipoResidencial = tipoResidencial;
    }

    public TipoLogradouro getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(TipoLogradouro tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}

