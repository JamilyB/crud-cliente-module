package com.ecommerce.ClienteModule.negocio;

import com.ecommerce.ClienteModule.domain.EntidadeDominio;

public interface
IStrategy<Entidade extends EntidadeDominio> {
    String processar(Entidade entidade);
}
