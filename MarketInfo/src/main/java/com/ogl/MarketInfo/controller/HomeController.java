package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Usuario;
import com.ogl.MarketInfo.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            description = "Retorna a página princiapl",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página home retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("usuario", usuarioService.getUsuarioLogado());
        model.addAttribute("roleLogada", usuarioService.getRolesUsuarioLogado());
        return "/home/home";
    }
}
