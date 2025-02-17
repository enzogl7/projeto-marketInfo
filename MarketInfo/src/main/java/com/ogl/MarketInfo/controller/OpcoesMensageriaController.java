package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.OpcoesMensageria;
import com.ogl.MarketInfo.service.ApiRequestService;
import com.ogl.MarketInfo.service.OpcoesMensageriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mensageria")
public class OpcoesMensageriaController {

    @Autowired
    private OpcoesMensageriaService opcoesMensageriaService;

    @Autowired
    private ApiRequestService apiRequestService;

    @Operation(
            summary = "Retorna a página de opções de mensageria",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de opções de mensageria retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/opcoes")
    public String opcoesMensageria(Model model) {
        OpcoesMensageria opcoesMensageria = opcoesMensageriaService.findAll().stream().findFirst().orElse(null);
        if (opcoesMensageria != null) {
            String emails = String.join(", ", opcoesMensageria.getEmails());
            model.addAttribute("emailsNotificacao", emails);
        }

        return "mensageria/opcoes";
    }

    @Operation(
            summary = "Cadastra/salva opções de mensageria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Opções salvas com sucesso!",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Opções salvas com sucesso!\"}"))),
                    @ApiResponse(responseCode = "400", description = "Erro ao salvar opções",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Erro ao salvar opções\"}")))
            }
    )
    @PostMapping("/salvaropcoes")
    public Object salvarOpcoes(@RequestParam("emailsNotificacao") String emailsNotificacao,
                               @RequestParam(value = "alertasEstoque", required = false) boolean alertasEstoque,
                               @RequestParam(value = "estoqueCadastro", required = false) boolean estoqueCadastro,
                               @RequestParam(value = "estoqueEdicao", required = false) boolean estoqueEdicao,
                               @RequestParam(value = "estoqueMinimo", required = false) boolean estoqueMinimo,
                               @RequestParam(value = "alertasProdutos", required = false) boolean alertasProdutos,
                               @RequestParam(value = "produtosCadastro", required = false) boolean produtosCadastro,
                               @RequestParam(value = "produtosEdicao", required = false) boolean produtosEdicao,
                               RedirectAttributes redirectAttributes,
                               HttpServletRequest request) {
        try {
            OpcoesMensageria op = opcoesMensageriaService.findAll().stream().findFirst().orElse(new OpcoesMensageria());

            if (emailsNotificacao != null && !emailsNotificacao.trim().isEmpty()) {
                List<String> listaEmails = Arrays.stream(emailsNotificacao.split(","))
                        .map(String::trim)
                        .collect(Collectors.toList());
                op.setEmails(listaEmails);
            }
            op.setAlertasEstoques(alertasEstoque);
            op.setEstoqueCadastro(Objects.requireNonNullElse(estoqueCadastro, false));
            op.setEstoqueEdicao(Objects.requireNonNullElse(estoqueEdicao, false));
            op.setEstoqueMinimo(Objects.requireNonNullElse(estoqueMinimo, false));

            op.setAlertasProdutos(alertasProdutos);
            op.setProdutosCadastro(Objects.requireNonNullElse(produtosCadastro, false));
            op.setProdutosEdicao(Objects.requireNonNullElse(produtosEdicao, false));

            opcoesMensageriaService.salvar(op);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Opções salvas com sucesso!");

            if (apiRequestService.isApiRequest(request)) {
                return ResponseEntity.ok("Opções salvas com sucesso!");
            }
            return "redirect:/mensageria/opcoes";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensagem", "Ocorreu um erro.");
            if (apiRequestService.isApiRequest(request)) {
                return ResponseEntity.badRequest().body("Erro ao salvar opções");
            }
            return "/mensageria/opcoes";

        }
    }
}
