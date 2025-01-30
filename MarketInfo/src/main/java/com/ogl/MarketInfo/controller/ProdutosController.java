package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Categoria;
import com.ogl.MarketInfo.model.Estoque;
import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.model.Usuario;
import com.ogl.MarketInfo.service.EstoqueService;
import com.ogl.MarketInfo.service.PrecoService;
import com.ogl.MarketInfo.service.ProdutosService;
import com.ogl.MarketInfo.service.UsuarioService;
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

    @GetMapping("/cadastrarProdutos")
    public String cadastrarProdutos() {
        return "/produtos/cadastrar_produtos";
    }

    @PostMapping("/salvarCadastroProduto")
    public String salvarCadastroProduto(@RequestParam("nomeProduto") String nomeProduto,
                                        @RequestParam("categoria") Categoria categoria,
                                        @RequestParam("marca") String marca,
                                        RedirectAttributes redirectAttributes)  {

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
        return "redirect:/produtos/cadastrarProdutos";
    }

    @GetMapping("/listarProdutos")
    public String listarProdutos(Model model) {
        List<Produtos> produtos = produtosService.listarTodos();
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("produtos", produtos);
        model.addAttribute("usuarios", usuarios);
        return "/produtos/listar_produtos";
    }

    @PostMapping("/editarProduto")
    public String editarProduto(@RequestParam("idProdutoEdicao") String idProdutoEdicao,
                                @RequestParam("nomeProdutoEdicao") String nomeProdutoEdicao,
                                @RequestParam("categoriaEdicao") Categoria categoriaEdicao,
                                @RequestParam("marcaEdicao")String marcaEdicao,
                                RedirectAttributes redirectAttributes)  {

        Produtos produto = produtosService.buscarPorId(Long.valueOf(idProdutoEdicao));
        produto.setNome(nomeProdutoEdicao);
        produto.setCategoria(categoriaEdicao);
        produto.setMarca(marcaEdicao);
        produto.setDataCadastro(produto.getDataCadastro());
        produto.setDataUltimaEdicao(LocalDate.now());
        produtosService.salvar(produto);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto editado com sucesso!");
        return "redirect:/produtos/listarProdutos";
    }

    @PostMapping("/excluirProduto")
    public ResponseEntity excluirProduto(@RequestParam("idProdutoExclusao") String idProdutoExclusao) {
        try {
            Produtos produtoExclusao = produtosService.buscarPorId(Long.valueOf(idProdutoExclusao));

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
