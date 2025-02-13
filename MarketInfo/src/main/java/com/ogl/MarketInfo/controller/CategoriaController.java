package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Categoria;
import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.model.Usuario;
import com.ogl.MarketInfo.service.CategoriaService;
import com.ogl.MarketInfo.service.ProdutosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.coyote.Response;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProdutosService produtosService;

    @Operation(
            description = "Retorna a página de gerenciamento de categorias",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de gerenciamento de categorias retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/gerenciamentoCategoria")
    public String gerenciamentoCategoria() {
        return "categoria/gerenciamento_categoria";
    }

    @Operation(
            description = "Retorna a página de cadastro de categorias",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de cadastro de categorias retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/cadastrarcategoria")
    public String cadastrarCategoria() {
        return "/categoria/cadastrar_categoria";
    }

    @Operation(
            description = "Cadastra/salva a categoria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoria salva com sucesso. Redireciona para a página de gerenciamento de categorias."),
                    @ApiResponse(responseCode = "302", description = "Erro ao cadastrar categoria. Redireciona para página de gerenciamento de categorias com a mensagem de erro")
            }
    )
    @PostMapping("/salvarCadastroCategoria")
    public String salvarCadastroCategoria(@RequestParam("nomeCategoria") String nomeCategoria,
                                          @RequestParam("descricaoCategoria") String descricaoCategoria,
                                          @RequestParam(value= "categoriaCheckbox", required = false) Boolean categoriaCheckbox,
                                          RedirectAttributes redirectAttributes) {
        try {
            Categoria categoria = new Categoria();

            categoria.setNome(nomeCategoria);
            categoria.setDescricao(descricaoCategoria);
            categoria.setStatus(categoriaCheckbox != null && categoriaCheckbox);
            categoria.setDataCriacao(LocalDate.now());
            categoria.setDataAtualizacao(null);
            categoriaService.salvar(categoria);

            redirectAttributes.addFlashAttribute("mensagemSucesso", "Categoria cadastrada com sucesso!");
            return "redirect:/categoria/gerenciamentoCategoria";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao cadastrar categoria.");
            return "redirect:/categoria/gerenciamentoCategoria";
        }
    }

    @Operation(
            description = "Retorna a página de listagem de categorias",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de listagem de categorias retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/listarcategoria")
    public String listarCategoria(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        return "/categoria/listar_categoria";
    }

    @Operation(
            description = "Edita a categoria já cadastrado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoria editada com sucesso."),
                    @ApiResponse(responseCode = "500", description = "Erro interno.")
            }
    )
    @PostMapping("/editarcategoria")
    public ResponseEntity editarCategoria(@RequestParam("idCategoriaEdicao") String idCategoriaEdicao,
                                          @RequestParam("nomeCategoriaEdicao") String nomeCategoriaEdicao,
                                          @RequestParam("descricaoCategoriaEdicao") String descricaoCategoriaEdicao,
                                          @RequestParam(value = "categoriaCheckboxEdicao", required = false) Boolean categoriaCheckboxEdicao) {

        Boolean categoriaVinculadaAAlgumProduto = produtosService.categoriaVinculadaAAlgumProduto(Long.valueOf(idCategoriaEdicao));
        Categoria categoria = categoriaService.findById(Long.valueOf(idCategoriaEdicao)).orElse(null);

        try {
            if (categoriaVinculadaAAlgumProduto && categoriaCheckboxEdicao == false) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
            } else {
                categoria.setNome(nomeCategoriaEdicao);
                categoria.setDescricao(descricaoCategoriaEdicao);
                categoria.setDataAtualizacao(LocalDate.now());
                categoria.setStatus(categoriaCheckboxEdicao != null && categoriaCheckboxEdicao);
                categoriaService.salvar(categoria);
                return ResponseEntity.ok().build();
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @Operation(
            description = "Exclui a categoria já cadastrada de acordo com o ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoria excluída com sucesso."),
                    @ApiResponse(responseCode = "500", description = "Erro interno.")
            }
    )
    @PostMapping("/excluircategoria")
    public ResponseEntity excluirCategoria(@RequestParam("idCategoriaExclusao") String idCategoriaExclusao) {
        Boolean categoriaVinculadaAAlgumProduto = produtosService.categoriaVinculadaAAlgumProduto(Long.valueOf(idCategoriaExclusao));

        try {
            if (categoriaVinculadaAAlgumProduto) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
            }
            categoriaService.deletar(Long.valueOf(idCategoriaExclusao));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
