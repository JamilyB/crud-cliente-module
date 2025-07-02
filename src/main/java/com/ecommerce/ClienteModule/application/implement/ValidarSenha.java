package com.ecommerce.ClienteModule.application.implement;

import com.ecommerce.ClienteModule.application.IStrategy;
import com.ecommerce.ClienteModule.domain.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ValidarSenha implements IStrategy<Cliente> {
    @Override
    public String processar(Cliente cliente) {
        return null;
    }
}
