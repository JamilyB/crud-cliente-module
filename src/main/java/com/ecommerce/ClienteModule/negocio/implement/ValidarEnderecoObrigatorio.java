package com.ecommerce.ClienteModule.negocio.implement;

import com.ecommerce.ClienteModule.negocio.IStrategy;
import com.ecommerce.ClienteModule.domain.Endereco;
import org.springframework.stereotype.Component;

@Component
public class ValidarEnderecoObrigatorio implements IStrategy<Endereco> {
    @Override
    public String processar(Endereco endereco) {
        return null;
    }
}
