package com.ecommerce.ClienteModule.application.implement;

import com.ecommerce.ClienteModule.application.IStrategy;
import com.ecommerce.ClienteModule.domain.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ValidarCPF implements IStrategy<Cliente> {
    @Override
    public String processar(Cliente cliente) {
        if (cliente.getCpf() == null || cliente.getCpf().length() != 11) {
            return "CPF inválido";
        }
        return null;
    }
}
