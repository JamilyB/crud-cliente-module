package com.ecommerce.ClienteModule.negocio.implement;

import com.ecommerce.ClienteModule.negocio.IStrategy;
import com.ecommerce.ClienteModule.domain.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ValidarSenha implements IStrategy<Cliente> {
    @Override
    public String processar(Cliente cliente) {
        return null;
    }
}
