package com.ecommerce.ClienteModule.application;

import com.ecommerce.ClienteModule.dao.implement.CartaoDAO;
import com.ecommerce.ClienteModule.dao.implement.ClienteDAO;
import com.ecommerce.ClienteModule.dao.implement.EnderecoDAO;
import com.ecommerce.ClienteModule.domain.*;
import com.ecommerce.ClienteModule.negocio.IStrategy;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Fachada {

    private ClienteDAO clienteDAO;
    private CartaoDAO cartaoDAO;
    private EnderecoDAO enderecoDAO;

    private List<IStrategy<Cliente>> regrasCliente;
    private List<IStrategy<Endereco>> regrasEndereco;
    private List<IStrategy<Cartao>> regrasCartao;

    public Fachada(ClienteDAO clienteDAO, CartaoDAO cartaoDAO, EnderecoDAO enderecoDAO,
            List<IStrategy<Cliente>> regrasCliente, List<IStrategy<Endereco>> regrasEndereco,
            List<IStrategy<Cartao>> regrasCartao) {

        this.clienteDAO = clienteDAO;
        this.cartaoDAO = cartaoDAO;
        this.enderecoDAO = enderecoDAO;
        this.regrasCliente = regrasCliente;
        this.regrasEndereco = regrasEndereco;
        this.regrasCartao = regrasCartao;
    }

    @Transactional
    public String salvar(Cliente cliente) {

        for (IStrategy<Cliente> regra : regrasCliente) {
            String erro = regra.processar(cliente);
            if (erro != null) return erro;
        }

        clienteDAO.save(cliente);

        for (Endereco e : cliente.getEnderecosCobranca()) {
            for (IStrategy<Endereco> regraEndereco : regrasEndereco) {
                String erro = regraEndereco.processar(e);
                if (erro != null) return erro;
            }
        }

        for (Endereco e : cliente.getEnderecosEntrega()) {
            for (IStrategy<Endereco> regraEndereco : regrasEndereco) {
                String erro = regraEndereco.processar(e);
                if (erro != null) return erro;
            }
        }

        for (Cartao c : cliente.getCartoes()) {
            for (IStrategy<Cartao> regraCartao : regrasCartao) {
                String erro = regraCartao.processar(c);
                if (erro != null) return erro;
            }
        }

        cliente.getEnderecosCobranca().forEach(enderecoDAO::save);
        cliente.getEnderecosEntrega().forEach(enderecoDAO::save);
        cliente.getCartoes().forEach(cartaoDAO::save);

        Long idCartaoPreferencial = cliente.getCartoes().get(0).getId();
        cliente.setIdCartaoPreferencial(idCartaoPreferencial);
        clienteDAO.atualizarCartaoPreferencial(cliente.getId(), idCartaoPreferencial);
        return null;
    }

    @Transactional
    public String atualizar(Cliente cliente) {
        for (IStrategy<Cliente> estrategia : regrasCliente) {
            String erro = estrategia.processar(cliente);
            if (erro != null) return erro;
        }
        clienteDAO.update(cliente);
        return null;
    }

    @Transactional
    public String atualizarCartoesPorCliente(Long clienteId, List<Cartao> cartoes) {
        for (Cartao c : cartoes) {
            for (IStrategy<Cartao> regraCartao : regrasCartao) {
                String erro = regraCartao.processar(c);
                if (erro != null) return erro;
            }
        }
        cartaoDAO.updatePorCliente(clienteId, cartoes);
        return null;
    }

    @Transactional
    public String atualizarEnderecosPorCliente(Long clienteId, List<Endereco> enderecos) {
        for (Endereco e : enderecos) {
            for (IStrategy<Endereco> regraEndereco : regrasEndereco) {
                String erro = regraEndereco.processar(e);
                if (erro != null) return erro;
            }
        }
        enderecoDAO.updatePorCliente(clienteId, enderecos);
        return null;
    }

    @Transactional
    public void excluirCliente(Long id) {
        clienteDAO.delete(id);
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.findAll();
    }

    public Bandeira buscarBandeiraPorId(Long id) {
        return cartaoDAO.findBandeiraById(id);
    }

    public TipoLogradouro buscarLogradouroPorId(Long id) {
        return enderecoDAO.findTipoLogradouroById(id);
    }

    public Cidade buscarCidadePorId(Long id) {
        return enderecoDAO.findCidadeById(id);
    }

    public Estado buscarEstadoPorId(Long id) {
        return enderecoDAO.findEstadoById(id);
    }

    public Cliente buscarClientePorId(Long id) {
        Cliente cliente = clienteDAO.findById(id);
        cliente.setEnderecosCobranca(enderecoDAO.findEnderecosCobrancaByClienteId(id));
        cliente.setEnderecosEntrega(enderecoDAO.findEnderecosEntregaByClienteId(id));
        cliente.setCartoes(cartaoDAO.buscarCartoesPorClienteId(id));
        return cliente;
    }

    public List<Bandeira> listarBandeiras() {
        return cartaoDAO.findAllBandeiras();
    }

    public List<TipoLogradouro> listarLogradouros() {
        return enderecoDAO.findAllLogradouro();
    }

    public List<Estado> listarEstados() {
        return enderecoDAO.findAllEstados();
    }

    public List<TipoResidencial> listarResidencial() {
        return enderecoDAO.findAllResidencial();
    }

    public List<TipoTelefone> listarTelefone() {
        return clienteDAO.findAllTelefone();
    }

    public List<Genero> listarGeneros() {
        return clienteDAO.findAllGeneros();
    }

    public List<DDD> listarDdds() {
        return clienteDAO.findAllDDD();
    }


}