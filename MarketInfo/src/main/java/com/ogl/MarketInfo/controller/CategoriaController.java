package com.ogl.MarketInfo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    @GetMapping("/gerenciamentoCategoria")
    public String gerenciamentoCategoria() {
        return "categoria/gerenciamento_categoria";
    }

    @GetMapping("/cadastrarcategoria")
    public String cadastrarCategoria() {
        return "/categoria/cadastrar_categoria";
    }
}
