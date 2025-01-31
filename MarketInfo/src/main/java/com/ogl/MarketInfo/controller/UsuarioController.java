package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Role;
import com.ogl.MarketInfo.model.Usuario;
import com.ogl.MarketInfo.service.RoleService;
import com.ogl.MarketInfo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/listausuario")
    public String listaUsuario(Model model) {
        model.addAttribute("usuarios", usuarioService.findAllComRoles());
        model.addAttribute("rolesSelect", roleService.findAll());
        return "usuarios/listar_usuarios";
    }

    @PostMapping("/editarusuario")
    public ResponseEntity editarUsuario(@RequestParam("idUsuarioEdicao")String idUsuarioEdicao,
                                        @RequestParam("emailEdicaoUsuario")String emailEdicaoUsuario,
                                        @RequestParam("nomeEdicaoUsuario")String nomeEdicaoUsuario,
                                        @RequestParam("senhaEdicaoUsuario")String senhaEdicaoUsuario,
                                        @RequestParam("selectRoleEdicaoUsuario") List<Long> selectRoleEdicaoUsuario,
                                        @RequestParam("checkboxUsuarioAtivo") boolean checkboxUsuarioAtivo) {
        try {
            Usuario usuario = usuarioService.findById(Long.valueOf(idUsuarioEdicao));

            usuario.setEmail(emailEdicaoUsuario);
            usuario.setUsername(nomeEdicaoUsuario);
            usuario.setEnabled(checkboxUsuarioAtivo);
            usuarioService.removerRolesDoUsuario(usuario.getId());

            for (Long roleId : selectRoleEdicaoUsuario) {
                usuarioService.associarRoleAoUsuario(usuario.getId(), roleId);
            }
            // TODO -> ERRO AQUI
            for (Role roleExistente : usuario.getRoles()) {
                if (!selectRoleEdicaoUsuario.contains(roleExistente.getId())) {
                    usuarioService.removerRoleDoUsuario(usuario.getId(), roleExistente.getId());
                }
            }
            if (senhaEdicaoUsuario != "") {
                usuario.setPassword(passwordEncoder.encode(senhaEdicaoUsuario));
            } else {
                usuario.setPassword(usuario.getPassword());
            }

            usuarioService.save(usuario);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
