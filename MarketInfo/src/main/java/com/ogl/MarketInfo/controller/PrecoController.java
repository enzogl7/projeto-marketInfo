package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Preco;
import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.service.ApiRequestService;
import com.ogl.MarketInfo.service.PrecoService;
import com.ogl.MarketInfo.service.ProdutosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/preco")
public class PrecoController {

    @Autowired
    private PrecoService precoService;

    @Autowired
    private ProdutosService produtosService;


    @Autowired
    private ApiRequestService apiRequestService;

    @Operation(
            summary = "Retorna a página de gerenciamento de preços",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de gerenciamento de preços retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/gerenciamentoPrecos")
    public String gerenciamentoPrecos() {
        return "preco/gerenciamento_precos";
    }

    @Operation(
            summary = "Retorna a página de cadastro de preços",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de cadastro de preços retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/cadastrarPrecos")
    public String cadastrarPrecos(Model model) {
        model.addAttribute("produtos", produtosService.listarTodos());
        return "preco/cadastro_preco";
    }


    @Operation(
            summary = "Cadastra/salva o preço.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Preço cadastrado com sucesso!",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Preço cadastrado com sucesso!\"}"))),

                    @ApiResponse(responseCode = "404", description = "Produto não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Produto não encontrado.\"}"))),

                    @ApiResponse(responseCode = "304", description = "Produto já possui preço cadastrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Produto já possui preço cadastrado.\"}")))
            }
    )
    @PostMapping("/salvarCadastroPreco")
    public Object salvarCadastroPreco(@RequestParam("produtoPreco")Long produtoId,
                                      @RequestParam("preco") String preco,
                                      @RequestParam("dataInicio") String dataInicio,
                                      @RequestParam("dataFinal") String dataFinal,
                                      RedirectAttributes redirectAttributes,
                                      HttpServletRequest request) {

        Produtos produto = produtosService.buscarPorId(produtoId).orElse(null);
        if (produto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
        }

        if (precoService.existePrecoParaEsseProduto(produto)) {
            redirectAttributes.addFlashAttribute("mensagem", "Produto já possui preço cadastrado!");
            if(apiRequestService.isApiRequest(request)) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Produto já possui preço cadastrado.");
            }
            return "redirect:/preco/gerenciamentoPrecos";
        }
        preco = preco.replace(',', '.');

        Preco p = new Preco();
        p.setProduto(produto);
        p.setPreco(Double.parseDouble(preco));
        p.setDataInicioVigor(LocalDate.parse(dataInicio));
        if (dataFinal == "") {
            p.setDataFimVigor(null);
        } else {
            p.setDataFimVigor(LocalDate.parse(dataFinal));
        }
        precoService.salvar(p);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Preço cadastrado com sucesso!");
        if(apiRequestService.isApiRequest(request)) {
            return ResponseEntity.ok().body("Preço cadastrado com sucesso!");
        }
        return "redirect:/preco/gerenciamentoPrecos";
    }

    @GetMapping("/listarPrecos")
    public String listarPrecos(Model model) {
        model.addAttribute("precos", precoService.listarTodos());
        model.addAttribute("produtos", produtosService.listarTodos());

        return "/preco/listar_precos";
    }


    @Operation(
            summary = "Obtém a lista de preços cadastrados.",
            description = "Retorna a lista de preços como JSON para ser exibida no Swagger. Na página da aplicação é retornada uma página com a tabela listando os preços."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de preços retornada com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Preco.class))
            )
    )
    @GetMapping("/listarPreco")
    @ResponseBody
    public List<Preco> listarPrecosJSON() {
        return precoService.listarTodos();
    }

    @Operation(
            summary = "Edita preços já cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Preço editado com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Preço editado com sucesso.\"}"))),

                    @ApiResponse(responseCode = "404", description = "Produto não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Produto não encontrado.\"}"))),

                    @ApiResponse(responseCode = "404", description = "Preço não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Preço não encontrado.\"}"))),

                    @ApiResponse(responseCode = "400", description = "Erro ao editar preço.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"erro\": \"Erro ao editar preço.\"}")))
            }
    )
    @PostMapping("/editarPreco")
    public ResponseEntity editarPreco(@RequestParam("idPrecoEdicao")Long idPrecoEdicao,
                              @RequestParam("produtoPrecoEdicao")String produtoPrecoEdicao,
                              @RequestParam("precoAtual")String precoAtual,
                              @RequestParam("dataInicioEdicao")String dataInicioEdicao,
                              @RequestParam("dataFinalEdicao")String dataFinalEdicao,
                              @RequestParam("motivoAlteracaoPreco") String motivoAlteracaoPreco) {
        Preco preco = precoService.buscaPorId(idPrecoEdicao);
        Produtos produto = produtosService.buscarPorId(Long.valueOf(produtoPrecoEdicao)).orElse(null);
        if (produto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
        }
        if (preco == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Preço não encontrado."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
        }

        try {
            precoAtual = precoAtual.replace("R$", "").trim();
            precoAtual = precoAtual.replace(',', '.');

            preco.setProduto(produto);
            preco.setPrecoAtual(Double.parseDouble(precoAtual));
            preco.setDataInicioVigor(LocalDate.parse(dataInicioEdicao));
            if (dataFinalEdicao == "") {
                preco.setDataFimVigor(null);
            } else {
                preco.setDataFimVigor(LocalDate.parse(dataFinalEdicao));
            }
            preco.setMotivoAlteracao(motivoAlteracaoPreco);
            preco.setDataAlteracao(LocalDate.now());
            precoService.salvar(preco);
            return ResponseEntity.ok().body("Preço editado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao editar preço.");
        }
    }

    @Operation(
            summary = "Exclui o preço de acordo com o ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Preço excluído com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Preço excluído com sucesso.\"}"))),

                    @ApiResponse(responseCode = "404", description = "Preço não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Preço não encontrado.\"}"))),

                    @ApiResponse(responseCode = "400", description = "Erro ao excluir Preço.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"erro\": \"Erro ao excluir Preço.\"}")))
            }
    )
    @PostMapping("/excluirPreco")
    public ResponseEntity excluirPreco(@RequestParam("idPrecoExclusao")Long idPrecoExclusao) {
        Preco preco = precoService.buscaPorId(idPrecoExclusao);
        if (preco == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Preço não encontrado."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
        }
        try {
            precoService.excluirPorId(idPrecoExclusao);
            return ResponseEntity.ok().body("Preço excluído com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao excluir preço;");
        }
    }
}
