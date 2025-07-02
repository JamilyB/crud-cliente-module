package com.ecommerce.ClienteModule.controller;

import com.ecommerce.ClienteModule.application.Fachada;
import com.ecommerce.ClienteModule.controller.util.ClienteForm;
import com.ecommerce.ClienteModule.controller.util.ClienteModel;
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

        ClienteModel.initEnderecos(fachada, cliente);
        ClienteModel.initCartoes(fachada, cliente);

        String erro = fachada.salvar(cliente);
        if (erro != null) {
            return ResponseEntity.badRequest().body(Map.of("erro", erro));
        }
        return ResponseEntity.ok(Map.of("sucesso", "Cliente salvo com sucesso!"));
    }

    @PostMapping("/editar")
    public ResponseEntity<?> editar(@ModelAttribute Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Cliente inválido"));
        }

        Cliente c = fachada.buscarClientePorId(cliente.getId());
        if (cliente.getSenha() == null || cliente.getSenha().isBlank()) {
            cliente.setSenha(c.getSenha());
        }

        ClienteModel.initEnderecos(fachada, cliente);
        fachada.atualizarEnderecosPorCliente(cliente.getId(), cliente.getEnderecosCobranca());
        fachada.atualizarEnderecosPorCliente(cliente.getId(), cliente.getEnderecosEntrega());

        ClienteModel.initCartoes(fachada, cliente);
        fachada.atualizarCartoesPorCliente(cliente.getId(), cliente.getCartoes());

        String erro = fachada.atualizar(cliente);
        if (erro != null) {
            return ResponseEntity.badRequest().body(Map.of("erro", erro));
        }
        return ResponseEntity.ok(Map.of("sucesso", "Cliente editado com sucesso!"));
    }

    @GetMapping("/cadastro")
    public String exibirFormulario(Model model) {
        model.addAttribute("cliente", new Cliente());
        ClienteForm.model(fachada, model);
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

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        Cliente cliente = fachada.buscarClientePorId(id);
        model.addAttribute("cliente", cliente);
        ClienteForm.model(fachada, model);
        model.addAttribute("modoEdicao", true);
        return "cadastro";
    }

}