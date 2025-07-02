package com.ecommerce.ClienteModule.negocio.implement;

import com.ecommerce.ClienteModule.negocio.IStrategy;
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
