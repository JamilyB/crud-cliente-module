package com.ecommerce.ClienteModule.application.implement;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.ecommerce.ClienteModule.application.IStrategy;
import com.ecommerce.ClienteModule.domain.Cliente;
import org.springframework.stereotype.Component;

@Component
public class CriptografarSenha implements IStrategy<Cliente> {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String processar(Cliente cliente) {
        if (cliente.getSenha() == null || cliente.getSenha().isEmpty()) {
            return "Senha não pode ser vazia";
        }

        String senhaCriptografada = encoder.encode(cliente.getSenha());
        cliente.setSenha(senhaCriptografada);

        return null;
    }
}
