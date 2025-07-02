package com.ecommerce.ClienteModule.negocio.implement;

import com.ecommerce.ClienteModule.negocio.IStrategy;
import com.ecommerce.ClienteModule.domain.Cliente;
import org.springframework.stereotype.Component;

@Component
public class GerarCodigoCliente implements IStrategy<Cliente> {

    @Override
    public String processar(Cliente cliente) {
        if (cliente.getCodigo() == null || cliente.getCodigo().isEmpty()) {
            String codigo = "CLI" + System.currentTimeMillis();
            cliente.setCodigo(codigo);
        }
        return null;
    }
}
