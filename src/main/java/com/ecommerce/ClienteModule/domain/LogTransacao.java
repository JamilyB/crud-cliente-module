package com.ecommerce.ClienteModule.domain;

import jakarta.persistence.Entity;

import java.time.LocalDate;
import java.time.LocalTime;
@Entity
public class LogTransacao extends EntidadeDominio {
    private LocalDate data;
    private LocalTime hora;
    private String usuario;

    public LogTransacao() {}

    public LogTransacao(LocalDate data, LocalTime hora, String usuario) {
        this.data = data;
        this.hora = hora;
        this.usuario = usuario;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
