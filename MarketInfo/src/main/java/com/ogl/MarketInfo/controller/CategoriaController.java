package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Categoria;
import com.ogl.MarketInfo.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/gerenciamentoCategoria")
    public String gerenciamentoCategoria() {
        return "categoria/gerenciamento_categoria";
    }

    @GetMapping("/cadastrarcategoria")
    public String cadastrarCategoria() {
        return "/categoria/cadastrar_categoria";
    }

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
}
