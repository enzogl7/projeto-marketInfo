package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Usuario;
import com.ogl.MarketInfo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("usuario", usuarioService.getUsuarioLogado());
        model.addAttribute("roleLogada", usuarioService.getRolesUsuarioLogado());
        return "/home/home";
    }
}
