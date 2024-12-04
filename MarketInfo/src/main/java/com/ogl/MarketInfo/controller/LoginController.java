package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Role;
import com.ogl.MarketInfo.model.Usuario;
import com.ogl.MarketInfo.repository.RoleRepository;
import com.ogl.MarketInfo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Controller
public class LoginController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public static class LoginRequest {
        private String username;
        private String senha;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }
    }

    @GetMapping("/login")
    public String login() {
        return "/login/login";
    }
    @GetMapping("/registro")
    public String registro() {
        return "/login/registro";
    }


    @PostMapping("/logar")
    public String logar(@RequestParam("username") String username,
                                   @RequestParam("senha") String senha) {

        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, senha);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                return "/home/home";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "/login/login";
        }
        return "redirect:/login";
    }

    @PostMapping("/registrar")
    @ResponseBody
    public ResponseEntity<?> registrar(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String email = payload.get("email");
        String senha = payload.get("senha");

        if (usuarioRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Este usuário já existe!"));
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(senha));
        usuario.setEnabled(true);

        Role roleUser = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new IllegalArgumentException("Role USER não encontrada"));
        usuario.setRoles(Collections.singleton(roleUser));

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of("message", "Usuário cadastrado com sucesso!"));
    }

}
