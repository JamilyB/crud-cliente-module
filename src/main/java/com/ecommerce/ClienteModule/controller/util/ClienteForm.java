package com.ecommerce.ClienteModule.controller.util;

import com.ecommerce.ClienteModule.application.Fachada;
import org.springframework.ui.Model;

public class ClienteForm {

    public static void model(Fachada fachada, Model model) {
        model.addAttribute("bandeiras", fachada.listarBandeiras());
        model.addAttribute("logradouros", fachada.listarLogradouros());
        model.addAttribute("estados", fachada.listarEstados());
        model.addAttribute("tiposResidencial", fachada.listarResidencial());
        model.addAttribute("tiposTelefone", fachada.listarTelefone());
        model.addAttribute("generos", fachada.listarGeneros());
        model.addAttribute("ddds", fachada.listarDdds());
    }
}
