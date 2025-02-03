package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Role;
import com.ogl.MarketInfo.service.RoleService;
import com.ogl.MarketInfo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/excluirperfil")
    public ResponseEntity excluirperfil(@RequestParam("idPerfilExclusao")String idPerfil) {
        Boolean perfilPossuiVinculoAAlgumUsuario = roleService.perfilPossuiVinculoAAlgumUsuario(Long.valueOf(idPerfil));
        try {
            if (perfilPossuiVinculoAAlgumUsuario) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
            }
            roleService.excluir(Long.valueOf(idPerfil));
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/editarperfil")
    public ResponseEntity editarPerfil(@RequestParam("idPerfilEdicao")String idPerfilEdicao,
                                       @RequestParam("nomePerfilEdicao") String nomePerfilEdicao,
                                       @RequestParam("descricaoPerfilEdicao") String descricaoPerfilEdicao) {
        try {
            Role role = roleService.findById(Long.valueOf(idPerfilEdicao));
            role.setRoleName(nomePerfilEdicao);
            if (descricaoPerfilEdicao == "") {
                role.setDescricao(null);
            } else {
                role.setDescricao(descricaoPerfilEdicao);
            }
            roleService.salvar(role);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
