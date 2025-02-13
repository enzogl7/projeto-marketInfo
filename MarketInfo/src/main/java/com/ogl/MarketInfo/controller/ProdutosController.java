package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Categoria;
import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.model.Usuario;
import com.ogl.MarketInfo.service.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/produtos")
public class ProdutosController {

    @Autowired
    private ProdutosService produtosService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    EstoqueService estoqueService;

    @Autowired
    PrecoService precoService;

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    ApiRequestService apiRequestService;

    @GetMapping("/gerenciamentoProdutos")
    public String gerenciamentoPrecos() {
        return "produtos/gerenciamento_produtos";
    }

    @GetMapping("/cadastrarProdutos")
    public String cadastrarProdutos(Model model) {
        model.addAttribute("categorias", categoriaService.findAllAtivo());
        return "/produtos/cadastrar_produtos";
    }


    @Operation(
            summary = "Cadastra/salva o produto.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto cadastrado com sucesso!",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Produto cadastrado com sucesso!\"}"))),

                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Categoria não encontrada.\"}"))),

                    @ApiResponse(responseCode = "400", description = "Erro ao cadastrar produto.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Erro ao cadastrar produto.\"}")))
            }
    )
    @PostMapping("/salvarCadastroProduto")
    public Object salvarCadastroProduto(@RequestParam("nomeProduto") String nomeProduto,
                                        @RequestParam("categoriaId") Long categoriaId,
                                        @RequestParam("marca") String marca,
                                        RedirectAttributes redirectAttributes,
                                        HttpServletRequest request)  {
        Categoria categoria = categoriaService.findById(categoriaId).orElse(null);
        if (categoria == null) {
            if (apiRequestService.isApiRequest(request)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
            }
        }

        Produtos produto = new Produtos();
        produto.setNome(nomeProduto);
        produto.setCategoria(categoria);
        produto.setMarca(marca);
        produto.setDataCadastro(LocalDate.now());
        produto.setDataUltimaEdicao(null);
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        produto.setUsuario(usuarioLogado);
        produtosService.salvar(produto);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto cadastrado com sucesso!");
        if (apiRequestService.isApiRequest(request)) {
            return ResponseEntity.ok().body("Produto cadastrado com sucesso!");
        }
        return "redirect:/produtos/cadastrarProdutos";
    }

    @GetMapping("/listarProdutos")
    public String listarProdutos(Model model) {
        List<Produtos> produtos = produtosService.listarTodos();
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("produtos", produtos);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("categorias", categoriaService.findAllAtivo());
        return "/produtos/listar_produtos";
    }

    @PostMapping("/editarProduto")
    public ResponseEntity editarProduto(@RequestParam("idProdutoEdicao") String idProdutoEdicao,
                                @RequestParam("nomeProdutoEdicao") String nomeProdutoEdicao,
                                @RequestParam("categoriaEdicao") String categoriaEdicao,
                                @RequestParam("marcaEdicao")String marcaEdicao)  {
        Categoria categoria = categoriaService.findById(Long.valueOf(categoriaEdicao)).orElse(null);
        try {
            Produtos produto = produtosService.buscarPorId(Long.valueOf(idProdutoEdicao)).orElse(null);
            produto.setNome(nomeProdutoEdicao);
            produto.setCategoria(categoria);
            produto.setMarca(marcaEdicao);
            produto.setDataCadastro(produto.getDataCadastro());
            produto.setDataUltimaEdicao(LocalDate.now());
            produtosService.salvar(produto);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PostMapping("/excluirProduto")
    public ResponseEntity excluirProduto(@RequestParam("idProdutoExclusao") String idProdutoExclusao) {
        try {
            Produtos produtoExclusao = produtosService.buscarPorId(Long.valueOf(idProdutoExclusao)).orElse(null);

            // verifica se o produto está vinculado à tabela de estoque ou preços
            Boolean produtoVinculadoATabelaEstoque = estoqueService.existeEstoqueParaEsseProduto(produtoExclusao);
            Boolean produtoVinculadoATabelaPreco = precoService.existePrecoParaEsseProduto(produtoExclusao);

            if (produtoVinculadoATabelaEstoque) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
            }
            if (produtoVinculadoATabelaPreco) {
                return ResponseEntity.status(HttpStatus.SEE_OTHER).build();
            }

            produtosService.excluir(Long.valueOf(idProdutoExclusao));

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
