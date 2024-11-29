package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Categoria;
import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.service.ProdutosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ProdutosController {

    @Autowired
    private ProdutosService produtosService;

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
        produtosService.salvar(produto);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto cadastrado com sucesso!");
        return "redirect:/cadastrarProdutos";
    }

    @GetMapping("/listarProdutos")
    public String listarProdutos(Model model) {
        List<Produtos> produtos = produtosService.listarTodos();
        model.addAttribute("produtos", produtos);
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
        return "redirect:/listarProdutos";
    }

    @PostMapping("/excluirProduto")
    public String excluirProduto(@RequestParam("idProdutoExclusao")String idProdutoExclusao,
                                 RedirectAttributes redirectAttributes)  {
        produtosService.excluir(Long.valueOf(idProdutoExclusao));

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto excluído com sucesso!");
        return "redirect:/listarProdutos";
    }

}
