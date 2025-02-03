package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Role;
import com.ogl.MarketInfo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/gerenciamentoPerfis")
    public String gerenciamentoPerfis() {
        return "perfil/gerenciamento_perfis";
    }

    @GetMapping("/cadastrarperfil")
    public String cadastrarPerfil(Model model) {
        return "perfil/cadastrar_perfil";
    }

    @PostMapping("/salvarCadastroPerfil")
    public String salvarCadastroPerfil(@RequestParam("nomePerfil") String nomePerfil,
                                       @RequestParam("descricaoPerfil") String descricaoPerfil,
                                       RedirectAttributes redirectAttributes) {
        Role role = new Role();
        role.setRoleName(nomePerfil);
        role.setDescricao(descricaoPerfil);
        roleService.salvar(role);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Perfil cadastrado com sucesso!");
        return "redirect:/perfil/gerenciamentoPerfis";
    }

    @GetMapping("/listarperfil")
    public String listarPerfil(Model model) {
        model.addAttribute("perfis", roleService.findAll());
        return "/perfil/listar_perfil";
    }

}
