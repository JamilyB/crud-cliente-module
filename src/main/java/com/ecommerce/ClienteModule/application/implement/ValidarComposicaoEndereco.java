package com.ecommerce.ClienteModule.application.implement;

import com.ecommerce.ClienteModule.application.IStrategy;
import com.ecommerce.ClienteModule.domain.Endereco;
import org.springframework.stereotype.Component;

@Component
public class ValidarComposicaoEndereco implements IStrategy<Endereco> {
    @Override
    public String processar(Endereco endereco) {
        if (endereco.getLogradouro() == null || endereco.getLogradouro().isEmpty()) {
            return "Logradouro não pode ser vazio";
        }
        if (endereco.getNumero() <= 0) {
            return "Número deve ser maior que zero";
        }
        if (endereco.getCep() == null || endereco.getCep().length() != 8) {
            return "CEP inválido";
        }
        return null;
    }
}
