package com.ogl.MarketInfo.controller;


import com.ogl.MarketInfo.model.Estoque;
import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.service.EstoqueService;
import com.ogl.MarketInfo.service.ProdutosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller("/estoque")
public class EstoqueController {
    @Autowired
    private ProdutosService produtosService;

    @Autowired
    private EstoqueService estoqueService;

    @GetMapping("/gerenciamentoEstoque")
    public String gerenciamentoEstoque() {
        return "estoque/gerenciamento_estoque";
    }

    @GetMapping("/cadastrarEstoque")
    public String cadastrarEstoque(Model model) {
        model.addAttribute("produtos", produtosService.listarTodos());
        return "estoque/cadastro_estoque";
    }

    @PostMapping("/salvarCadastroEstoque")
    public String salvarCadastroEstoque(@RequestParam("produtoEstoque")Produtos produtos,
                                        @RequestParam("estoqueMinimo")String estoqueMinimo,
                                        @RequestParam("estoqueAtual")String estoqueAtual,
                                        RedirectAttributes redirectAttributes) {

        if (estoqueService.existeEstoqueParaEsseProduto(produtos)) {
            redirectAttributes.addFlashAttribute("mensagem", "Este produto já possui estoque cadastrado!");
            return "redirect:/gerenciamentoEstoque";
        }

        Estoque estoque = new Estoque();
        estoque.setProduto(produtos);
        estoque.setQtdeEstoqueMinimo(Integer.valueOf(estoqueMinimo));
        estoque.setQtdeEstoqueAtual(Integer.valueOf(estoqueAtual));
        estoqueService.salvar(estoque);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Estoque cadastrado com sucesso!");
        return "redirect:/gerenciamentoEstoque";
    }

    @GetMapping("/listarEstoque")
    public String listarEstoque(Model model) {
        model.addAttribute("produtos", produtosService.listarTodos());
        model.addAttribute("estoques", estoqueService.findAll());
        return "/estoque/listar_estoque";
    }

    @PostMapping("/editarEstoque")
    public String editarEstoque(@RequestParam("idEstoqueEdicao") String idEstoqueEdicao,
                                @RequestParam("produtoEstoqueEdicao") Produtos produtoEstoqueEdicao,
                                @RequestParam("estoqueMinimoEdicao") String estoqueMinimoEdicao,
                                @RequestParam("estoqueAtualEdicao") String estoqueAtualEdicao,
                                RedirectAttributes redirectAttributes) {

        Estoque estoque = estoqueService.findById(Long.valueOf(idEstoqueEdicao));
        estoque.setProduto(produtoEstoqueEdicao);
        estoque.setQtdeEstoqueMinimo(Integer.valueOf(estoqueMinimoEdicao));
        estoque.setQtdeEstoqueAtual(Integer.valueOf(estoqueAtualEdicao));
        estoqueService.salvar(estoque);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Estoque editado com sucesso!");
        return "redirect:/gerenciamentoEstoque";
    }

    @PostMapping("/excluirEstoque")
    public String excluirEstoque(@RequestParam("idEstoqueExclusao") String idEstoqueExclusao,
                                 RedirectAttributes redirectAttributes) {
        estoqueService.excluirPorId(Long.valueOf(idEstoqueExclusao));

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Estoque excluído com sucesso!");
        return "redirect:/gerenciamentoEstoque";
    }


}
