package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Categoria;
import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.service.ProdutosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

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
        produtosService.salvar(produto);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto cadastrado com sucesso!");
        return "redirect:/cadastrarProdutos";
    }


}
