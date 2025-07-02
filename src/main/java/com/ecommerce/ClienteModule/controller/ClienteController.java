package com.ecommerce.ClienteModule.controller;

import com.ecommerce.ClienteModule.application.Fachada;
import com.ecommerce.ClienteModule.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private Fachada fachada;

    @PostMapping
    public ResponseEntity<?> salvar(@ModelAttribute Cliente cliente) {

        if (cliente.getEnderecosCobranca() != null) {
            for (Endereco e : cliente.getEnderecosCobranca()) {
                e.setTipo("COBRANCA");
                e.setCliente(cliente);
                e.setTipoLogradouro(fachada.buscarLogradouroPorId(e.getTipoLogradouro().getId()));

                if (e.getCidade() != null && e.getCidade().getId() != null) {
                    Cidade cidade = fachada.buscarCidadePorId(e.getCidade().getId());
                    if (cidade != null) {
                        e.setCidade(cidade);
                        if (cidade.getEstado() != null && cidade.getEstado().getId() != null) {
                            Estado estado = fachada.buscarEstadoPorId(cidade.getEstado().getId());
                            cidade.setEstado(estado);
                        }
                    }
                }
            }
        }

        for (Endereco e : cliente.getEnderecosCobranca()) {
            if (e.getCidade() == null) {
                System.out.println("Entrega: cidade nula");
            } else if (e.getCidade().getId() == null) {
                System.out.println("Entrega: cidade id nulo");
            } else {
                System.out.println("Entrega: cidade id = " + e.getCidade().getId());
            }
        }

        if (cliente.getEnderecosEntrega() != null) {
            for (Endereco e : cliente.getEnderecosEntrega()) {
                e.setTipo("ENTREGA");
                e.setCliente(cliente);
                e.setTipoLogradouro(fachada.buscarLogradouroPorId(e.getTipoLogradouro().getId()));

                if (e.getCidade() != null && e.getCidade().getId() != null) {
                    Cidade cidade = fachada.buscarCidadePorId(e.getCidade().getId());
                    if (cidade != null) {
                        e.setCidade(cidade);
                        if (cidade.getEstado() != null && cidade.getEstado().getId() != null) {
                            Estado estado = fachada.buscarEstadoPorId(cidade.getEstado().getId());
                            cidade.setEstado(estado);
                        }
                    }
                }
            }
        }

        for (Endereco e : cliente.getEnderecosEntrega()) {
            if (e.getCidade() == null) {
                System.out.println("Entrega: cidade nula");
            } else if (e.getCidade().getId() == null) {
                System.out.println("Entrega: cidade id nulo");
            } else {
                System.out.println("Entrega: cidade id = " + e.getCidade().getId());
            }
        }

        if (cliente.getCartoes() != null) {
            for (Cartao c : cliente.getCartoes()) {
                c.setCliente(cliente);
                c.setBandeira(fachada.buscarBandeiraPorId(c.getBandeira().getId()));
            }
        }

        String erro = fachada.salvar(cliente);
        if (erro != null) {
            return ResponseEntity.badRequest().body(Map.of("erro", erro));
        }
        return ResponseEntity.ok(Map.of("sucesso", "Cliente salvo com sucesso!"));
    }

    @GetMapping("/cadastro")
    public String exibirFormulario(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("bandeiras", fachada.listarBandeiras());
        model.addAttribute("logradouros", fachada.listarLogradouros());
        model.addAttribute("estados", fachada.listarEstados());
        model.addAttribute("tiposResidencial", fachada.listarResidencial());
        model.addAttribute("tiposTelefone", fachada.listarTelefone());
        model.addAttribute("generos", fachada.listarGeneros());
        model.addAttribute("ddds", fachada.listarDdds());
        return "cadastro";
    }

    @GetMapping("/listar")
    public String listarClientes(Model model) {
        model.addAttribute("clientes", fachada.listarClientes());
        return "clientes";
    }

    @GetMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable Long id, Model model) {
        fachada.excluirCliente(id);
        return "redirect:/clientes/listar";
    }

    @PostMapping("/editar")
    public ResponseEntity<?> editar(@ModelAttribute Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Cliente inválido"));
        }

        if (cliente.getEnderecosCobranca() != null) {
            for (Endereco e : cliente.getEnderecosCobranca()) {
                e.setTipo("COBRANCA");
                e.setCliente(cliente);
                e.setTipoLogradouro(fachada.buscarLogradouroPorId(e.getTipoLogradouro().getId()));
                fachada.atualizarEnderecosPorCliente(cliente.getId(), cliente.getEnderecosCobranca());
                if (e.getCidade() != null) {
                    Cidade cidade = fachada.buscarCidadePorId(e.getCidade().getId());
                    e.setCidade(cidade);
                    if (cidade != null && cidade.getEstado() != null) {
                        cidade.setEstado(fachada.buscarEstadoPorId(cidade.getEstado().getId()));
                    }
                }
            }
        }

        if (cliente.getEnderecosEntrega() != null) {
            for (Endereco e : cliente.getEnderecosEntrega()) {
                e.setTipo("ENTREGA");
                e.setCliente(cliente);
                e.setTipoLogradouro(fachada.buscarLogradouroPorId(e.getTipoLogradouro().getId()));
                fachada.atualizarEnderecosPorCliente(cliente.getId(), cliente.getEnderecosEntrega());
                if (e.getCidade() != null) {
                    Cidade cidade = fachada.buscarCidadePorId(e.getCidade().getId());
                    e.setCidade(cidade);
                    if (cidade != null && cidade.getEstado() != null) {
                        cidade.setEstado(fachada.buscarEstadoPorId(cidade.getEstado().getId()));
                    }
                }
            }
        }
        if (cliente.getCartoes() != null) {
            for (Cartao c : cliente.getCartoes()) {
                c.setCliente(cliente);
                c.setBandeira(fachada.buscarBandeiraPorId(c.getBandeira().getId()));
            }
            fachada.atualizarCartoesPorCliente(cliente.getId(), cliente.getCartoes());
        }
        String erro = fachada.atualizarCliente(cliente);
        if (erro != null) {
            return ResponseEntity.badRequest().body(Map.of("erro", erro));
        }
        return ResponseEntity.ok(Map.of("sucesso", "Cliente editado com sucesso!"));
    }

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        Cliente cliente = fachada.buscarClientePorId(id);
        model.addAttribute("cliente", cliente);
        model.addAttribute("bandeiras", fachada.listarBandeiras());
        model.addAttribute("logradouros", fachada.listarLogradouros());
        model.addAttribute("estados", fachada.listarEstados());
        model.addAttribute("tiposResidencial", fachada.listarResidencial());
        model.addAttribute("tiposTelefone", fachada.listarTelefone());
        model.addAttribute("generos", fachada.listarGeneros());
        model.addAttribute("ddds", fachada.listarDdds());
        model.addAttribute("modoEdicao", true); // flag
        return "cadastro";
    }
}