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
        if (precoService.existePrecoParaEsseProduto(produtos)) {
            redirectAttributes.addFlashAttribute("mensagem", "Produto já possui preço cadastrado!");
            return "redirect:/gerenciamentoPrecos";
        }

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
        model.addAttribute("produtos", produtosService.listarTodos());
        return "/preco/listar_precos";
    }

    @PostMapping("/editarPreco")
    public String editarPreco(@RequestParam("idPrecoEdicao")String idPrecoEdicao,
                              @RequestParam("produtoPrecoEdicao")Produtos produtoPrecoEdicao,
                              @RequestParam("precoAtual")String precoAtual,
                              @RequestParam("dataInicioEdicao")String dataInicioEdicao,
                              @RequestParam("dataFinalEdicao")String dataFinalEdicao,
                              @RequestParam("motivoAlteracaoPreco") String motivoAlteracaoPreco,
                              RedirectAttributes redirectAttributes) {

        Preco p = precoService.buscaPorId(Long.valueOf(idPrecoEdicao));
        p.setProduto(produtoPrecoEdicao);
        p.setPrecoAtual(Double.parseDouble(precoAtual));
        p.setDataInicioVigor(LocalDate.parse(dataInicioEdicao));
        p.setDataFimVigor(LocalDate.parse(dataFinalEdicao));
        p.setMotivoAlteracao(motivoAlteracaoPreco);
        precoService.salvar(p);


        redirectAttributes.addFlashAttribute("mensagemSucesso", "Preço editado com sucesso!");
        return "redirect:/gerenciamentoPrecos";
    }
}
