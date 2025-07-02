package com.ecommerce.ClienteModule.application.implement;

import com.ecommerce.ClienteModule.application.IStrategy;
import com.ecommerce.ClienteModule.domain.Cliente;
import org.springframework.stereotype.Component;

@Component
public class DefinirStatusCliente implements IStrategy<Cliente> {

    private final String statusPadrao;

    public DefinirStatusCliente() {
        this.statusPadrao = "Ativo";
    }

    public DefinirStatusCliente(String status) {
        this.statusPadrao = status;
    }

    @Override
    public String processar(Cliente cliente) {
        if (cliente.getStatus() == null || cliente.getStatus().isBlank()) {
            cliente.setStatus(statusPadrao);
        }
        return null;
    }
}
