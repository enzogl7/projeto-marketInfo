package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Role;
import com.ogl.MarketInfo.service.ApiRequestService;
import com.ogl.MarketInfo.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private ApiRequestService apiRequestService;


    @Operation(
            description = "Retorna a página de gerenciamento de perfis (roles)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de gerenciamento de perfis retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/gerenciamentoPerfis")
    public String gerenciamentoPerfis() {
        return "perfil/gerenciamento_perfis";
    }

    @Operation(
            description = "Retorna a página de cadastro de perfis (roles)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de cadastro de perfis retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/cadastrarperfil")
    public String cadastrarPerfil(Model model) {
        return "perfil/cadastrar_perfil";
    }

    @Operation(
            description = "Cadastra/salva o perfil",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil salvo com sucesso. Redireciona para a página de gerenciamento de perfis.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Perfil criado com sucesso!\"}"))),
                    @ApiResponse(responseCode = "400", description = "Erro ao salvar perfil. Redireciona para página de gerenciamento de perfis com a mensagem de erro",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Erro ao salvar perfil.\"}")))
            }
    )
    @PostMapping("/salvarCadastroPerfil")
    public Object salvarCadastroPerfil(@RequestParam("nomePerfil") String nomePerfil,
                                       @RequestParam("descricaoPerfil") String descricaoPerfil,
                                       RedirectAttributes redirectAttributes,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        try {
            Role role = new Role();
            role.setRoleName(nomePerfil);
            role.setDescricao(descricaoPerfil);
            roleService.salvar(role);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Perfil cadastrado com sucesso!");
            if (apiRequestService.isApiRequest(request)) {
                return ResponseEntity.ok("Perfil cadastrado com sucesso!");
            }
            return "redirect:/perfil/gerenciamentoPerfis";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Ocorreu um erro.");

            if (apiRequestService.isApiRequest(request)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao salvar perfil");
            }

            return "/login/login";
        }

    }

    @Operation(
            description = "Retorna a página de listagem de perfis",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de listagem de perfis retornada com sucesso",
                            content = @Content(mediaType = "text/html")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @GetMapping("/listarperfil")
    public String listarPerfil(Model model) {
        model.addAttribute("perfis", roleService.findAll());
        return "/perfil/listar_perfil";
    }

    @Operation(
            description = "Exclui o perfil.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil excluído com sucesso!",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Perfil excluído com sucesso!\"}"))),

                    @ApiResponse(responseCode = "404", description = "Erro ao excluir perfil. Perfil não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Erro ao excluir perfil. Perfil não encontrado.\"}"))),

                    @ApiResponse(responseCode = "304", description = "Erro ao excluir perfil. Perfil vinculado a algum usuário.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Erro ao excluir perfil. Perfil vinculado a algum usuário.\"}"))),


                    @ApiResponse(responseCode = "400", description = "Erro ao excluir perfil.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Erro ao excluir perfil.\"}")))
            }
    )
    @PostMapping("/excluirperfil")
    public ResponseEntity excluirperfil(@RequestParam("idPerfilExclusao")String idPerfil,
                                        HttpServletRequest request) {
        Boolean perfilPossuiVinculoAAlgumUsuario = roleService.perfilPossuiVinculoAAlgumUsuario(Long.valueOf(idPerfil));
        Optional<Role> role = roleService.findById(Long.valueOf(idPerfil));

        try {
            if (role.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao excluir perfil. Perfil não encontrado."); // não encontrou perfil com o id
            }

            if (perfilPossuiVinculoAAlgumUsuario) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Erro ao excluir perfil. Perfil vinculado a algum usuário."); // perfil possui vinculo a algum usuario
            }

            roleService.excluir(Long.valueOf(idPerfil));
            return ResponseEntity.ok("Perfil excluído com sucesso!"); // sucesso
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao excluir perfil.");
        }
    }

    @Operation(
            description = "Edita o perfil já cadastrado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil editado com sucesso."),
                    @ApiResponse(responseCode = "500", description = "Erro interno.")
            }
    )
    @PostMapping("/editarperfil")
    public ResponseEntity editarPerfil(@RequestParam("idPerfilEdicao")String idPerfilEdicao,
                                       @RequestParam("nomePerfilEdicao") String nomePerfilEdicao,
                                       @RequestParam("descricaoPerfilEdicao") String descricaoPerfilEdicao) {
        try {
            Role role = roleService.findById(Long.valueOf(idPerfilEdicao)).orElseThrow(null);
            role.setRoleName(nomePerfilEdicao);
            if (descricaoPerfilEdicao == "") {
                role.setDescricao(null);
            } else {
                role.setDescricao(descricaoPerfilEdicao);
            }
            roleService.salvar(role);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
