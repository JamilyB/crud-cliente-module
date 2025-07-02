package com.ecommerce.ClienteModule.application.implement;

import com.ecommerce.ClienteModule.application.IStrategy;
import com.ecommerce.ClienteModule.domain.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ValidarDadosCliente implements IStrategy<Cliente> {
    @Override
    public String processar(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            return "Nome é obrigatório";
        }
        if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            return "Email é obrigatório";
        }
        return null;
    }
}
