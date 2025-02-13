package com.ogl.MarketInfo.controller;


import com.ogl.MarketInfo.model.Estoque;
import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.model.Role;
import com.ogl.MarketInfo.service.ApiRequestService;
import com.ogl.MarketInfo.service.EstoqueService;
import com.ogl.MarketInfo.service.ProdutosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {
    @Autowired
    private ProdutosService produtosService;

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private ApiRequestService apiRequestService;

    @Operation(
            summary = "Retorna a página de gerenciamento de estoque",
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
            summary = "Retorna a página de cadastro de estoque",
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
            summary = "Cadastra/salva o estoque.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estoque cadastrado com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Estoque cadastrado com sucesso.\"}"))),

                    @ApiResponse(responseCode = "404", description = "Produto não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Produto não encontrado.\"}"))),

                    @ApiResponse(responseCode = "406", description = "Este produto já possui estoque cadastrado!",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"erro\": \"Este produto já possui estoque cadastrado!\"}")))
            }
    )
    @PostMapping("/salvarCadastroEstoque")
    public Object salvarCadastroEstoque(@RequestParam("produtoId")Long produtoId,
                                        @RequestParam("estoqueMinimo")String estoqueMinimo,
                                        @RequestParam("estoqueAtual")String estoqueAtual,
                                        RedirectAttributes redirectAttributes,
                                        HttpServletRequest request) {
        Produtos produto = produtosService.buscarPorId(produtoId).orElse(null);
        if (produto == null) {
            if (apiRequestService.isApiRequest(request)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
            }
        }

        if (estoqueService.existeEstoqueParaEsseProduto(produto)) {
            redirectAttributes.addFlashAttribute("mensagem", "Este produto já possui estoque cadastrado!"); // mensagem html
            if (apiRequestService.isApiRequest(request)) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Este produto já possui estoque cadastrado!"); // mensagem swagger
            }
            return "redirect:/estoque/gerenciamentoEstoque";
        }

        Estoque estoque = new Estoque();
        estoque.setProduto(produto);
        estoque.setQtdeEstoqueMinimo(Integer.valueOf(estoqueMinimo));
        estoque.setQtdeEstoqueAtual(Integer.valueOf(estoqueAtual));
        estoqueService.salvar(estoque);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Estoque cadastrado com sucesso!");
        if (apiRequestService.isApiRequest(request)) {
            return ResponseEntity.ok("Estoque cadastrado com sucesso");
        }
        return "redirect:/estoque/gerenciamentoEstoque";
    }

    @GetMapping("/listarEstoque")
    public String listarEstoque(Model model) {
        model.addAttribute("produtos", produtosService.listarTodos());
        model.addAttribute("estoques", estoqueService.findAll());
        return "/estoque/listar_estoque";
    }


    @Operation(
            summary = "Obtém a lista de estoques cadastrados.",
            description = "Retorna a lista de estoques como JSON para ser exibida no Swagger. Na página da aplicação é retornada uma página com a tabela listando com os estoques dos respectivos produtos."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de estoque retornada com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Estoque.class))
            )
    )
    @GetMapping("/listarestoques")
    @ResponseBody
    public List<Estoque> listarEstoqueJson(){
        return estoqueService.findAll();
    }

    @Operation(
            summary = "Edita o estoque de produtos já cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estoque editado com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Estoque editado com sucesso.\"}"))),

                    @ApiResponse(responseCode = "404", description = "Estoque não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Estoque não encontrado.\"}"))),

                    @ApiResponse(responseCode = "400", description = "Erro ao editar estoque.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"erro\": \"Erro ao editar estoque.\"}")))
            }
    )
    @PostMapping("/editarEstoque")
    public ResponseEntity editarEstoque(@RequestParam("idEstoqueEdicao") String idEstoqueEdicao,
                                                   @RequestParam("produtoEstoqueEdicao") Long produtoEstoqueEdicaoId,
                                                   @RequestParam("estoqueMinimoEdicao") String estoqueMinimoEdicao,
                                                   @RequestParam("estoqueAtualEdicao") String estoqueAtualEdicao) {
        Estoque estoque = estoqueService.findById(Long.valueOf(idEstoqueEdicao)).orElse(null);
        Produtos produto = produtosService.buscarPorId(produtoEstoqueEdicaoId).orElse(null);

        if (estoque == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estoque não encontrado."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
        }
        if (produto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.

        }
        try {
            estoque.setProduto(produto);
            estoque.setQtdeEstoqueMinimo(Integer.valueOf(estoqueMinimoEdicao));
            estoque.setQtdeEstoqueAtual(Integer.valueOf(estoqueAtualEdicao));
            estoqueService.salvar(estoque);

            return ResponseEntity.ok().body("Estoque editado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao editar estoque.");
        }
    }

    @Operation(
            summary = "Exclui o estoque de acordo com o ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estoque excluído com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Estoque excluído com sucesso.\"}"))),

                    @ApiResponse(responseCode = "404", description = "Estoque não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Estoque não encontrado.\"}"))),

                    @ApiResponse(responseCode = "400", description = "Erro ao editar estoque.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"erro\": \"Erro ao editar estoque.\"}")))
            }
    )
    @PostMapping("/excluirEstoque")
    public ResponseEntity excluirEstoque(@RequestParam("idEstoqueExclusao") String idEstoqueExclusao) {
        Estoque estoque = estoqueService.findById(Long.valueOf(idEstoqueExclusao)).orElse(null);
        if (estoque == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estoque não encontrado."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
        }
        try {
            estoqueService.excluirPorId(Long.valueOf(idEstoqueExclusao));
            return ResponseEntity.ok().body("Estoque excluído com sucesso.");
        } catch(Exception e) {
            return ResponseEntity.badRequest().body("Erro ao editar estoque.");
        }

    }


}
