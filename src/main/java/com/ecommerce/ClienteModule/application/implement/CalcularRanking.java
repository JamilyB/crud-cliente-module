package com.ecommerce.ClienteModule.application.implement;

import com.ecommerce.ClienteModule.application.IStrategy;
import com.ecommerce.ClienteModule.domain.Cliente;
import org.springframework.stereotype.Component;

@Component
public class CalcularRanking implements IStrategy<Cliente> {

    @Override
    public String processar(Cliente cliente) {
        if (cliente.getRanking() == null) {
            cliente.setRanking(1000);
        }
        return null;
    }
}
