package com.ecommerce.ClienteModule.application;

import com.ecommerce.ClienteModule.domain.EntidadeDominio;

public interface
IStrategy<Entidade extends EntidadeDominio> {
    String processar(Entidade entidade);
}
