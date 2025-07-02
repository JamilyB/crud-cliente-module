package com.ecommerce.ClienteModule.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
public class Cliente extends EntidadeDominio {

    private String codigo;
    private String status;
    private String nome;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "genero_id")
    private Genero genero;
    private String dtNasc;
    private String cpf;
    private String email;

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
    private Telefone telefone;
    private String senha;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Cartao> cartoes;
    private Long idCartaoPreferencial;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecosCobranca;
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecosEntrega;

    private Integer ranking;
    @OneToMany
    private List<Transacao> transacoes;

    public Cliente(String codigo, String status, String nome, Genero genero, String dtNasc, String cpf, String email,
                   Telefone telefone, String senha, List<Cartao> cartoes, Long idCartaoPreferencial,
                   List<Endereco> enderecosCobranca, List<Endereco> enderecosEntrega, List<Transacao> transacoes, Integer ranking) {

        this.codigo = codigo;
        this.status = status;
        this.nome = nome;
        this.genero = genero;
        this.dtNasc = dtNasc;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.cartoes = cartoes;
        this.idCartaoPreferencial = idCartaoPreferencial;
        this.enderecosCobranca = enderecosCobranca;
        this.enderecosEntrega = enderecosEntrega;
        this.ranking = ranking;
    }

    public Cliente() {

    }


}

