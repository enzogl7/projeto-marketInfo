package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Categoria;
import com.ogl.MarketInfo.model.Estoque;
import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.model.Usuario;
import com.ogl.MarketInfo.service.ApiRequestService;
import com.ogl.MarketInfo.service.CategoriaService;
import com.ogl.MarketInfo.service.ProdutosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private ApiRequestService apiRequestService;

    @Operation(
            summary = "Retorna a página de gerenciamento de categorias",
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
            summary = "Retorna a página de cadastro de categorias",
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
            summary = "Cadastra/salva a categoria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoria cadastrada com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Categoria cadastrada com sucesso.\"}"))),

                    @ApiResponse(responseCode = "400", description = "Erro ao cadastrar categoria.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Erro ao cadastrar categoria.\"}")))
            }
    )
    @PostMapping("/salvarCadastroCategoria")
    public Object salvarCadastroCategoria(@RequestParam("nomeCategoria") String nomeCategoria,
                                          @RequestParam("descricaoCategoria") String descricaoCategoria,
                                          @RequestParam(value= "categoriaCheckbox", required = false) Boolean categoriaCheckbox,
                                          RedirectAttributes redirectAttributes,
                                          HttpServletRequest request) {
        try {
            Categoria categoria = new Categoria();

            categoria.setNome(nomeCategoria);
            categoria.setDescricao(descricaoCategoria);
            categoria.setStatus(categoriaCheckbox != null && categoriaCheckbox);
            categoria.setDataCriacao(LocalDate.now());
            categoria.setDataAtualizacao(null);
            categoriaService.salvar(categoria);

            redirectAttributes.addFlashAttribute("mensagemSucesso", "Categoria cadastrada com sucesso!");
            if (apiRequestService.isApiRequest(request)) {
                return ResponseEntity.ok().body("Categoria cadastrada com sucesso.");
            }
            return "redirect:/categoria/gerenciamentoCategoria";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao cadastrar categoria.");
            if (apiRequestService.isApiRequest(request)) {
                return ResponseEntity.badRequest().body("Erro ao cadastrar categoria");
            }
            return "redirect:/categoria/gerenciamentoCategoria";
        }
    }


    @GetMapping("/listarcategoria")
    public String listarCategoria(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        return "/categoria/listar_categoria";
    }


    @Operation(
            summary = "Obtém a lista de categorias cadastradas.",
            description = "Retorna a lista de categorias como JSON para ser exibida no Swagger. Na página da aplicação é retornada uma página com a tabela listando as categorias cadastradas."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de categorias retornada com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Categoria.class))
            )
    )
    @GetMapping("/listarcategorias")
    @ResponseBody
    public List<Categoria> listarCategoriaJSON() {
        return categoriaService.findAll();
    }

    @Operation(
            summary = "Edita a categoria já cadastrado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoria editada com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Categoria editada com sucesso.\"}"))),

                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Categoria não encontrada.\"}"))),

                    @ApiResponse(responseCode = "304", description = "Não foi possível excluir esta categoria pois está vinculado à produto e/ou está inativa.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Não foi possível excluir esta categoria pois está vinculado à produto e/ou está inativa.\"}"))),

                    @ApiResponse(responseCode = "400", description = "Erro ao editar categoria.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"erro\": \"Erro ao editar categoria.\"}")))
            }
    )
    @PostMapping("/editarcategoria")
    public ResponseEntity editarCategoria(@RequestParam("idCategoriaEdicao") String idCategoriaEdicao,
                                          @RequestParam("nomeCategoriaEdicao") String nomeCategoriaEdicao,
                                          @RequestParam("descricaoCategoriaEdicao") String descricaoCategoriaEdicao,
                                          @RequestParam(value = "categoriaCheckboxEdicao", required = false) Boolean categoriaCheckboxEdicao) {

        Boolean categoriaVinculadaAAlgumProduto = produtosService.categoriaVinculadaAAlgumProduto(Long.valueOf(idCategoriaEdicao));
        Categoria categoria = categoriaService.findById(Long.valueOf(idCategoriaEdicao)).orElse(null);
        if (categoria == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
        }

        try {
            if (categoriaVinculadaAAlgumProduto && categoriaCheckboxEdicao == false) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Não foi possível excluir esta categoria pois está vinculado à produto e/ou está inativa.");
            } else {
                categoria.setNome(nomeCategoriaEdicao);
                categoria.setDescricao(descricaoCategoriaEdicao);
                categoria.setDataAtualizacao(LocalDate.now());
                categoria.setStatus(categoriaCheckboxEdicao != null && categoriaCheckboxEdicao);
                categoriaService.salvar(categoria);
                return ResponseEntity.ok().body("Categoria editada com sucesso.");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao editar categoria.");
        }

    }

    @Operation(
            summary = "Exclui a categoria já cadastrada de acordo com o ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoria excluída com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Categoria excluída com sucesso.\"}"))),

                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Categoria não encontrada.\"}"))),

                    @ApiResponse(responseCode = "304", description = "Não foi possível excluir essa categoria pois está vinculada a algum produto.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Não foi possível excluir essa categoria pois está vinculada a algum produto.\"}"))),

                    @ApiResponse(responseCode = "400", description = "Erro ao excluir categoria.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Erro ao excluir categoria.\"}"))),
            }
    )
    @PostMapping("/excluircategoria")
    public ResponseEntity excluirCategoria(@RequestParam("idCategoriaExclusao") String idCategoriaExclusao) {
        Boolean categoriaVinculadaAAlgumProduto = produtosService.categoriaVinculadaAAlgumProduto(Long.valueOf(idCategoriaExclusao));
        Categoria categoria = categoriaService.findById(Long.valueOf(idCategoriaExclusao)).orElse(null);
        if (categoria == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
        }

        try {
            if (categoriaVinculadaAAlgumProduto) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Não foi possível excluir essa categoria pois está vinculada a algum produto.");
            }

            categoriaService.deletar(Long.valueOf(idCategoriaExclusao));
            return ResponseEntity.ok().body("Categoria excluída com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao excluir categoria.");
        }
    }
}
