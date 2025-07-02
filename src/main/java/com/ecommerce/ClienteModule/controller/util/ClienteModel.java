package com.ecommerce.ClienteModule.controller.util;

import com.ecommerce.ClienteModule.application.Fachada;
import com.ecommerce.ClienteModule.domain.*;

public class ClienteModel {

    public static void initEnderecos(Fachada fachada, Cliente cliente) {
        if (cliente.getEnderecosCobranca() != null) {
            for (Endereco e : cliente.getEnderecosCobranca()) {
                e.setTipo("COBRANCA");
                e.setCliente(cliente);
                e.setTipoLogradouro(fachada.buscarLogradouroPorId(e.getTipoLogradouro().getId()));

                if (e.getCidade() != null && e.getCidade().getId() != null) {
                    Cidade cidade = fachada.buscarCidadePorId(e.getCidade().getId());
                    if (cidade != null) {
                        e.setCidade(cidade);
                        if (cidade.getEstado() != null && cidade.getEstado().getId() != null) {
                            Estado estado = fachada.buscarEstadoPorId(cidade.getEstado().getId());
                            cidade.setEstado(estado);
                        }
                    }
                }
            }
        }

        if (cliente.getEnderecosEntrega() != null) {
            for (Endereco e : cliente.getEnderecosEntrega()) {
                e.setTipo("ENTREGA");
                e.setCliente(cliente);
                e.setTipoLogradouro(fachada.buscarLogradouroPorId(e.getTipoLogradouro().getId()));

                if (e.getCidade() != null && e.getCidade().getId() != null) {
                    Cidade cidade = fachada.buscarCidadePorId(e.getCidade().getId());
                    if (cidade != null) {
                        e.setCidade(cidade);
                        if (cidade.getEstado() != null && cidade.getEstado().getId() != null) {
                            Estado estado = fachada.buscarEstadoPorId(cidade.getEstado().getId());
                            cidade.setEstado(estado);
                        }
                    }
                }
            }
        }
    }

    public static void initCartoes(Fachada fachada, Cliente cliente) {
        if (cliente.getCartoes() != null) {
            for (Cartao c : cliente.getCartoes()) {
                c.setCliente(cliente);
                c.setBandeira(fachada.buscarBandeiraPorId(c.getBandeira().getId()));
            }
        }
    }
}
