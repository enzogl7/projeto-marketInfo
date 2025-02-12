package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Role;
import com.ogl.MarketInfo.model.Usuario;
import com.ogl.MarketInfo.repository.RoleRepository;
import com.ogl.MarketInfo.repository.UsuarioRepository;
import com.ogl.MarketInfo.service.ApiRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private ApiRequestService apiRequestService;

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


    @Operation(
            description = "Retorna a página de login",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de login retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/login")
    public String login() {
        return "/login/login";
    }

    @Operation(
            description = "Retorna a página de registro",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de registro retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/registro")
    public String registro() {
        return "/login/registro";
    }

    @Operation(
            description = "Autentica o usuário com base no email e senha fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso. Redireciona para a página home.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Login realizado com sucesso!\"}"))),
                    @ApiResponse(responseCode = "406", description = "Falha na autenticação. Redireciona para a página de login com mensagem de erro.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"erro\": \"Usuário não encontrado.\"}"))),
                    @ApiResponse(responseCode = "400", description = "Erro desconhecido.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"erro\": \"Erro desconhecido.\"}")))
            }
    )
    @PostMapping("/logar")
    public Object logar(@RequestParam("email") String email,
                       @RequestParam("senha") String senha,
                        RedirectAttributes redirectAttributes,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        try {
            if (usuarioOptional.isPresent()) {
                if (!passwordEncoder.matches(senha, usuarioOptional.get().getPassword())) {
                    redirectAttributes.addFlashAttribute("mensagem", "Senha incorreta.");

                    if (apiRequestService.isApiRequest(request)) {
                        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Senha incorreta.");
                    }

                    return "redirect:/login";
                }
            } else {
                redirectAttributes.addFlashAttribute("mensagem", "Usuário não encontrado.");

                if (apiRequestService.isApiRequest(request)) {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Usuário não encontrado");
                }

                return "redirect:/login";
            }

            // sucesso
            Usuario usuario = usuarioOptional.get();
            if (!usuario.isEnabled()) {
                redirectAttributes.addFlashAttribute("mensagem", "Este usuário está inativo.");

                if (apiRequestService.isApiRequest(request)) {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Usuário inativo.");
                }

                return "redirect:/login";
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(usuario.getUsername(), senha, usuario.getAuthorities());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);

            HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
            securityContextRepository.saveContext(context, request, response);

            if (apiRequestService.isApiRequest(request)) {
                return ResponseEntity.ok("Logado com sucesso!");
            }
            return "redirect:/home";
        }
         catch (Exception e) {
             redirectAttributes.addFlashAttribute("mensagem", "Ocorreu um erro.");

             if (apiRequestService.isApiRequest(request)) {
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocorreu um erro");
             }

            return "/login/login";
        }
    }

    @Operation(
            description = "Registra/cria o usuário no banco de dados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cadastro realizado com sucesso. Redireciona para a página de login.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Usuário criado com sucesso!\"}"))),
                    @ApiResponse(responseCode = "406", description = "Usuário ou email já existente. Redireciona para a página de registro com mensagem de erro.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"erro\": \"Usuário ou email já existente!\"}")))
            }
    )
    @PostMapping("/registrar")
    public Object registrar(@RequestParam String username,
                            @RequestParam String senha,
                            @RequestParam String email,
                            RedirectAttributes redirectAttributes,
                            HttpServletRequest request) {

        if (usuarioRepository.findByUsername(username).isPresent()) {
            redirectAttributes.addFlashAttribute("mensagem", "Este usuário já existe!");

            if (apiRequestService.isApiRequest(request)) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Usuário já existe!");
            }

            return "redirect:/registro";
        }

        if (usuarioRepository.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("mensagem", "Email já cadastrado!");

            if (apiRequestService.isApiRequest(request)) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Email já cadastrado!");
            }

            return "redirect:/registro";
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(senha));
        usuario.setEnabled(true);

        Role roleUser = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new IllegalArgumentException("Role USER não encontrada"));
        usuario.setRoles(Collections.singletonList(roleUser));

        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário criado com sucesso!");

        if (apiRequestService.isApiRequest(request)) {
            return ResponseEntity.ok("Usuário criado com sucesso!");
        }

        return "redirect:/login";
    }

}
