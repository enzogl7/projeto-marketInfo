package com.ogl.MarketInfo.controller;


import com.ogl.MarketInfo.model.Estoque;
import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.service.EstoqueService;
import com.ogl.MarketInfo.service.ProdutosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {
    @Autowired
    private ProdutosService produtosService;

    @Autowired
    private EstoqueService estoqueService;

    @Operation(
            description = "Retorna a página de gerenciamento de estoque",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de gerenciamento de estoque retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/gerenciamentoEstoque")
    public String gerenciamentoEstoque() {
        return "estoque/gerenciamento_estoque";
    }

    @Operation(
            description = "Retorna a página de cadastro de estoque",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de cadastro de estoque retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/cadastrarEstoque")
    public String cadastrarEstoque(Model model) {
        model.addAttribute("produtos", produtosService.listarTodos());
        return "estoque/cadastro_estoque";
    }

    @Operation(
            description = "Cadastra/salva o estoque",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estoque salvo com sucesso. Redireciona para a página de gerenciamento de estoque."),
                    @ApiResponse(responseCode = "302", description = "Produto com estoque já cadastrado. Redireciona para página de gerenciamento de estoque com a mensagem de erro")
            }
    )
    @PostMapping("/salvarCadastroEstoque")
    public String salvarCadastroEstoque(@RequestParam("produtoEstoque")Produtos produtos,
                                        @RequestParam("estoqueMinimo")String estoqueMinimo,
                                        @RequestParam("estoqueAtual")String estoqueAtual,
                                        RedirectAttributes redirectAttributes) {

        if (estoqueService.existeEstoqueParaEsseProduto(produtos)) {
            redirectAttributes.addFlashAttribute("mensagem", "Este produto já possui estoque cadastrado!");
            return "redirect:/estoque/gerenciamentoEstoque";
        }

        Estoque estoque = new Estoque();
        estoque.setProduto(produtos);
        estoque.setQtdeEstoqueMinimo(Integer.valueOf(estoqueMinimo));
        estoque.setQtdeEstoqueAtual(Integer.valueOf(estoqueAtual));
        estoqueService.salvar(estoque);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Estoque cadastrado com sucesso!");
        return "redirect:/estoque/gerenciamentoEstoque";
    }

    @Operation(
            description = "Retorna a página de listagem de estoque",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de listagem de estoque retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/listarEstoque")
    public String listarEstoque(Model model) {
        model.addAttribute("produtos", produtosService.listarTodos());
        model.addAttribute("estoques", estoqueService.findAll());
        return "/estoque/listar_estoque";
    }

    @Operation(
            description = "Edita o estoque já cadastrado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estoque editado com sucesso."),
                    @ApiResponse(responseCode = "500", description = "Erro interno.")
            }
    )
    @PostMapping("/editarEstoque")
    public ResponseEntity editarEstoque(@RequestParam("idEstoqueEdicao") String idEstoqueEdicao,
                                                   @RequestParam("produtoEstoqueEdicao") Produtos produtoEstoqueEdicao,
                                                   @RequestParam("estoqueMinimoEdicao") String estoqueMinimoEdicao,
                                                   @RequestParam("estoqueAtualEdicao") String estoqueAtualEdicao) {

        try {
            Estoque estoque = estoqueService.findById(Long.valueOf(idEstoqueEdicao));
            estoque.setProduto(produtoEstoqueEdicao);
            estoque.setQtdeEstoqueMinimo(Integer.valueOf(estoqueMinimoEdicao));
            estoque.setQtdeEstoqueAtual(Integer.valueOf(estoqueAtualEdicao));
            estoqueService.salvar(estoque);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            description = "Exclui o estoque já cadastrado de acordo com o ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estoque excluído com sucesso."),
                    @ApiResponse(responseCode = "500", description = "Erro interno.")
            }
    )
    @PostMapping("/excluirEstoque")
    public ResponseEntity excluirEstoque(@RequestParam("idEstoqueExclusao") String idEstoqueExclusao) {
        try {
            estoqueService.excluirPorId(Long.valueOf(idEstoqueExclusao));
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }


}
