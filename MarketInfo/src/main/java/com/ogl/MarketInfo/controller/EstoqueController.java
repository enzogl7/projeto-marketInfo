package com.ogl.MarketInfo.controller;


import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.service.ProdutosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EstoqueController {
    @Autowired
    private ProdutosService produtosService;

    @GetMapping("/gerenciamentoEstoque")
    public String gerenciamentoEstoque() {
        return "estoque/gerenciamento_estoque";
    }

    @GetMapping("/cadastrarEstoque")
    public String cadastrarEstoque(Model model) {
        model.addAttribute("produtos", produtosService.listarTodos());
        return "estoque/cadastro_estoque";
    }


}
