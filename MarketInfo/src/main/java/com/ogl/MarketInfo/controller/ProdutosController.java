package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.*;
import com.ogl.MarketInfo.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
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

    @Autowired
    private OpcoesMensageriaService opcoesMensageriaService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Operation(
            summary = "Retorna a página de gerenciamento de produtos",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de gerenciamento de produtos retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/gerenciamentoProdutos")
    public String gerenciamentoPrecos() {
        return "produtos/gerenciamento_produtos";
    }

    @Operation(
            summary = "Retorna a página de cadastro de produtos",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de cadastro de produtos retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
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
        OpcoesMensageria opcoesMensageria = opcoesMensageriaService.findAll().stream().findFirst().orElse(null);

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

        if (opcoesMensageria != null && opcoesMensageria.isProdutosCadastro()) {
            kafkaTemplate.send("produtos-alert", "Um novo produto foi cadastrado! Produto: " + produto.getNome()
                    + " || Categoria: " + (produto.getCategoria().getNome())
                    + " || Marca: " + (produto.getMarca())
                    + " || Cadastrado por: " + (usuarioLogado.getEmail()));
        }


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


    @Operation(
            summary = "Obtém a lista de produtos cadastrados.",
            description = "Retorna a lista de produtos como JSON para ser exibida no Swagger. Na página da aplicação é retornada uma página com a tabela listando os produtos."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de produtos retornada com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Estoque.class))
            )
    )
    @GetMapping("/listarProduto")
    @ResponseBody
    public List<Produtos> listarProdutosJSON() {
        return produtosService.listarTodos();
    }

    @Operation(
            summary = "Edita produtos já cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto editado com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Produto editado com sucesso.\"}"))),

                    @ApiResponse(responseCode = "404", description = "Produto/categoria não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Produto/categoria não encontrado.\"}"))),

                    @ApiResponse(responseCode = "400", description = "Erro ao editar Produto.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"erro\": \"Erro ao editar Produto.\"}")))
            }
    )
    @PostMapping("/editarProduto")
    public ResponseEntity editarProduto(@RequestParam("idProdutoEdicao") String idProdutoEdicao,
                                @RequestParam("nomeProdutoEdicao") String nomeProdutoEdicao,
                                @RequestParam("categoriaEdicao") String categoriaEdicao,
                                @RequestParam("marcaEdicao")String marcaEdicao)  {
        Categoria categoria = categoriaService.findById(Long.valueOf(categoriaEdicao)).orElse(null);
        Produtos produto = produtosService.buscarPorId(Long.valueOf(idProdutoEdicao)).orElse(null);
        OpcoesMensageria opcoesMensageria = opcoesMensageriaService.findAll().stream().findFirst().orElse(null);
        if (produto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
        }
        if (categoria == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
        }
        try {
            produto.setNome(nomeProdutoEdicao);
            produto.setCategoria(categoria);
            produto.setMarca(marcaEdicao);
            produto.setDataCadastro(produto.getDataCadastro());
            produto.setDataUltimaEdicao(LocalDate.now());
            produtosService.salvar(produto);
            if (opcoesMensageria != null && opcoesMensageria.isProdutosCadastro()) {
                kafkaTemplate.send("produtos-alert", "Um produto foi editado! Produto: " + produto.getNome()
                        + " || Categoria: " + (produto.getCategoria().getNome())
                        + " || Marca: " + (produto.getMarca())
                        + " || Data de edição: " + (produto.getDataUltimaEdicao().toString())
                        + " || Editado por: " + (usuarioService.getUsuarioLogado().getEmail()));
            }

            return ResponseEntity.ok().body("Produto editado com sucesso");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao editar Produto.");
        }

    }

    @Operation(
            summary = "Exclui o produto de acordo com o ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto excluído com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Produto excluído com sucesso.\"}"))),

                    @ApiResponse(responseCode = "404", description = "Produto não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Produto não encontrado.\"}"))),

                    @ApiResponse(responseCode = "304", description = "Não foi possível excluir este produto pois está vinculado à algum registro de estoque.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Não foi possível excluir este produto pois está vinculado à algum registro de estoque.\"}"))),

                    @ApiResponse(responseCode = "303", description = "Não foi possível excluir este produto pois está vinculado à algum registro de preço.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Não foi possível excluir este produto pois está vinculado à algum registro de preço.\"}"))),

                    @ApiResponse(responseCode = "400", description = "Erro ao excluir Produto.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"erro\": \"Erro ao excluir Produto.\"}")))
            }
    )
    @PostMapping("/excluirProduto")
    public ResponseEntity excluirProduto(@RequestParam("idProdutoExclusao") String idProdutoExclusao) {
        try {
            Produtos produtoExclusao = produtosService.buscarPorId(Long.valueOf(idProdutoExclusao)).orElse(null);
            if (produtoExclusao == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
            }

            // verifica se o produto está vinculado à tabela de estoque ou preços
            Boolean produtoVinculadoATabelaEstoque = estoqueService.existeEstoqueParaEsseProduto(produtoExclusao);
            Boolean produtoVinculadoATabelaPreco = precoService.existePrecoParaEsseProduto(produtoExclusao);

            if (produtoVinculadoATabelaEstoque) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Não foi possível excluir este produto pois está vinculado à algum registro de estoque.");
            }
            if (produtoVinculadoATabelaPreco) {
                return ResponseEntity.status(HttpStatus.SEE_OTHER).body("Não foi possível excluir este produto pois está vinculado à algum registro de preço.");
            }

            produtosService.excluir(Long.valueOf(idProdutoExclusao));

            return ResponseEntity.ok().body("Produto excluído com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao excluir produto.");
        }
    }


}
