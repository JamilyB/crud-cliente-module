package com.ecommerce.ClienteModule.application.implement;

import com.ecommerce.ClienteModule.application.IStrategy;
import com.ecommerce.ClienteModule.domain.Endereco;
import org.springframework.stereotype.Component;

@Component
public class ValidarEnderecoObrigatorio implements IStrategy<Endereco> {

    @Override
    public String processar(Endereco endereco) {
        return null;
    }
}
