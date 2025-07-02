package com.ecommerce.ClienteModule.application.implement;

import com.ecommerce.ClienteModule.application.IStrategy;
import com.ecommerce.ClienteModule.domain.Cartao;
import org.springframework.stereotype.Component;

@Component
public class ValidarCartaoCredito implements IStrategy<Cartao> {

    @Override
    public String processar(Cartao cartao) {
        if (cartao == null) {
            return "Cartão não informado";
        }
        if (cartao.getNumCartao() == null || cartao.getNumCartao().isBlank()) {
            return "Número do cartão é obrigatório";
        }
        if (cartao.getNomeImpresso() == null || cartao.getNomeImpresso().isBlank()) {
            return "Nome impresso no cartão é obrigatório";
        }
        if (cartao.getCodSeguranca() == null || cartao.getCodSeguranca().isBlank()) {
            return "Código de segurança é obrigatório";
        }
        if (cartao.getBandeira() == null) {
            return "Bandeira do cartão é obrigatória";
        }
        if (cartao.getBandeira().getId() == null) {
            return "Bandeira do cartão não registrada no sistema";
        }
        return null;
    }
}
