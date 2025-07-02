package com.ecommerce.ClienteModule.application;

import com.ecommerce.ClienteModule.dao.CartaoDAO;
import com.ecommerce.ClienteModule.dao.ClienteDAO;
import com.ecommerce.ClienteModule.dao.EnderecoDAO;
import com.ecommerce.ClienteModule.domain.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Fachada {

    private ClienteDAO clienteDAO;
    private CartaoDAO cartaoDAO;
    private EnderecoDAO enderecoDAO;

    private List<IStrategy<Cliente>> estrategias;

    public Fachada(ClienteDAO clienteDAO,CartaoDAO cartaoDAO, List<IStrategy<Cliente>> estrategias, EnderecoDAO enderecoDAO) {
        this.clienteDAO = clienteDAO;
        this.estrategias = estrategias;
        this.cartaoDAO = cartaoDAO;
        this.enderecoDAO = enderecoDAO;
    }

    @Transactional
    public String salvar(Cliente cliente) {

        for (IStrategy<Cliente> estrategia : estrategias) {
            String erro = estrategia.processar(cliente);
            if (erro != null) return erro;
        }
        clienteDAO.save(cliente);
        for (Endereco e : cliente.getEnderecosCobranca()) {
            enderecoDAO.save(e);
        }
        for (Endereco e : cliente.getEnderecosEntrega()) {
            enderecoDAO.save(e);
        }
        for (Cartao c : cliente.getCartoes()) {
            cartaoDAO.save(c);
        }
        Long idCartaoPreferencial = cliente.getCartoes().get(0).getId();
        cliente.setIdCartaoPreferencial(idCartaoPreferencial);
        clienteDAO.atualizarCartaoPreferencial(cliente.getId(), idCartaoPreferencial);
        return null;
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


    public Object listarClientes() {
        return clienteDAO.findAll();
    }

    @Transactional
    public String atualizarCliente(Cliente cliente) {
        for (IStrategy<Cliente> estrategia : estrategias) {
            String erro = estrategia.processar(cliente);
            if (erro != null) return erro;
        }
        clienteDAO.update(cliente);
        return null;
    }

    @Transactional
    public void atualizarCartoesPorCliente(Long clienteId, List<Cartao> cartoes) {
        cartaoDAO.updatePorCliente(clienteId, cartoes);
    }

    @Transactional
    public void atualizarEnderecosPorCliente(Long clienteId, List<Endereco> enderecos) {
        enderecoDAO.updatePorCliente(clienteId, enderecos);
    }

    @Transactional
    public void excluirCliente(Long id) {
        clienteDAO.delete(id);
    }

    public Cliente buscarClientePorId(Long id) {
        Cliente cliente = clienteDAO.findById(id);
        cliente.setEnderecosCobranca(enderecoDAO.findEnderecosCobrancaByClienteId(id));
        cliente.setEnderecosEntrega(enderecoDAO.findEnderecosEntregaByClienteId(id));
        cliente.setCartoes(cartaoDAO.buscarCartoesPorClienteId(id));
        return cliente;
    }

}