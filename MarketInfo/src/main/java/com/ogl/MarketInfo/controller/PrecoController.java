package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Preco;
import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.service.PrecoService;
import com.ogl.MarketInfo.service.ProdutosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class PrecoController {

    @Autowired
    private PrecoService precoService;

    @Autowired
    private ProdutosService produtosService;


    @GetMapping("/gerenciamentoPrecos")
    public String gerenciamentoPrecos() {
        return "preco/gerenciamento_precos";
    }

    @GetMapping("/cadastrarPrecos")
    public String cadastrarPrecos(Model model) {
        model.addAttribute("produtos", produtosService.listarTodos());
        return "preco/cadastro_preco";
    }

    @PostMapping("/salvarCadastroPreco")
    public String salvarCadastroPreco(@RequestParam("produtoPreco")Produtos produtos,
                                      @RequestParam("preco") String preco,
                                      @RequestParam("dataInicio") String dataInicio,
                                      @RequestParam("dataFinal") String dataFinal,
                                      RedirectAttributes redirectAttributes) {
        Preco p = new Preco();
        p.setProduto(produtos);
        p.setPreco(Double.parseDouble(preco));
        p.setDataInicioVigor(LocalDate.parse(dataInicio));
        p.setDataFimVigor(LocalDate.parse(dataFinal));
        precoService.salvar(p);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Preço cadastrado com sucesso!");
        return "redirect:/gerenciamentoPrecos";
    }

    @GetMapping("/listarPrecos")
    public String listarPrecos(Model model) {
        model.addAttribute("precos", precoService.listarTodos());
        return "/preco/listar_precos";

    }
}
