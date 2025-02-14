package com.ogl.MarketInfo.controller;

import com.ogl.MarketInfo.model.Preco;
import com.ogl.MarketInfo.model.Role;
import com.ogl.MarketInfo.model.Usuario;
import com.ogl.MarketInfo.service.ProdutosService;
import com.ogl.MarketInfo.service.RoleService;
import com.ogl.MarketInfo.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProdutosService produtosService;

    @GetMapping("/listausuario")
    public String listaUsuario(Model model) {
        model.addAttribute("roleLogada", usuarioService.getRolesUsuarioLogado());
        model.addAttribute("usuarios", usuarioService.findAllComRoles());
        model.addAttribute("rolesSelect", roleService.findAll());
        return "usuarios/listar_usuarios";
    }

    @Operation(
            summary = "Obtém a lista de usuários cadastrados.",
            description = "Retorna a lista de usuários como JSON para ser exibida no Swagger. Na página da aplicação é retornada uma página com a tabela listando os usuários."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de usuários retornada com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Preco.class))
            )
    )
    @GetMapping("/listarusuarios")
    @ResponseBody
    public List<Usuario> listaUsuarioJSON() {
        return usuarioService.findAllComRoles();
    }

    @Operation(
            summary = "Edita usuários já cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário editado com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Usuário editado com sucesso.\"}"))),

                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Usuário não encontrado.\"}"))),

                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Perfil/role não encontrado.\"}"))),

                    @ApiResponse(responseCode = "400", description = "Erro ao editar Usuário.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"erro\": \"Erro ao editar Usuário.\"}")))
            }
    )
    @PostMapping("/editarusuario")
    public ResponseEntity editarUsuario(@RequestParam("idUsuarioEdicao")String idUsuarioEdicao,
                                        @RequestParam("emailEdicaoUsuario")String emailEdicaoUsuario,
                                        @RequestParam("nomeEdicaoUsuario")String nomeEdicaoUsuario,
                                        @RequestParam("senhaEdicaoUsuario")String senhaEdicaoUsuario,
                                        @RequestParam("selectRoleEdicaoUsuario") List<Long> selectRoleEdicaoUsuario,
                                        @RequestParam("checkboxUsuarioAtivo") boolean checkboxUsuarioAtivo) {
        try {
            Usuario usuario = usuarioService.findById(Long.valueOf(idUsuarioEdicao)).orElse(null);
            List<Role> roles = new ArrayList<>();
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
            }
            for (Long id : selectRoleEdicaoUsuario) {
                Role role = roleService.findById(id).orElse(null);
                if (role != null) {
                    roles.add(role);
                }
            }
            if (roles.isEmpty()) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Perfil/role não encontrado."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
            }

            usuario.setEmail(emailEdicaoUsuario);
            usuario.setUsername(nomeEdicaoUsuario);
            usuario.setEnabled(checkboxUsuarioAtivo);
            usuarioService.removerRolesDoUsuario(usuario.getId());

            for (Long roleId : selectRoleEdicaoUsuario) {
                usuarioService.associarRoleAoUsuario(usuario.getId(), roleId);
            }

            if (senhaEdicaoUsuario != "") {
                usuario.setPassword(passwordEncoder.encode(senhaEdicaoUsuario));
            } else {
                usuario.setPassword(usuario.getPassword());
            }

            usuarioService.save(usuario);
            return ResponseEntity.ok().body("Usuário editado com sucesso.");
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao editar usuário.");
        }
    }


    @Operation(
            summary = "Exclui o usuário de acordo com o ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário excluído com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Usuário excluído com sucesso.\"}"))),

                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Usuário não encontrado.\"}"))),

                    @ApiResponse(responseCode = "304", description = "Não foi possível excluir este usuário pois está vinculado à algum registro de produto.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Não foi possível excluir este usuário pois está vinculado à algum registro de produto.\"}"))),

                    @ApiResponse(responseCode = "400", description = "Erro ao excluir usuário.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"erro\": \"Erro ao excluir usuário.\"}")))
            }
    )
    @PostMapping("/excluirusuario")
    public ResponseEntity excluirUsuario(@RequestParam("idUsuarioExclusao")String idUsuario) {
        Boolean usuarioPossuiVinculoAAlgumProduto = produtosService.usuarioPossuiVinculoAAlgumProduto(Long.valueOf(idUsuario));
        Usuario usuario = usuarioService.findById(Long.valueOf(idUsuario)).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
        }

        try {
            if (usuarioPossuiVinculoAAlgumProduto) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Não foi possível excluir este usuário pois está vinculado à algum registro de produto.");
            }
            usuarioService.removerRolesDoUsuario(Long.valueOf(idUsuario));
            usuarioService.excluirUsuario(Long.valueOf(idUsuario));
            return ResponseEntity.ok().body("Usuário excluído com sucesso.");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao excluir usuário.");
        }
    }

    @Operation(
            summary = "Inativa o usuário de acordo com o ID.",
            description = "Essa opção é recomendada ao usuário caso ele tente excluir um usuário que possua vínculo à algum registro de produto.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário inativado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Usuário inativado com sucesso\"}"))),

                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"mensagem\": \"Usuário não encontrado.\"}"))),

                    @ApiResponse(responseCode = "400", description = "Erro ao inativar usuário.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"erro\": \"Erro ao inativar usuário.\"}")))
            }
    )
    @PostMapping("/inativarusuario")
    public ResponseEntity inativarUsuario(@RequestParam("idUsuarioInativacao")String idUsuarioInativacao) {
        Usuario usuario = usuarioService.findById(Long.valueOf(idUsuarioInativacao)).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado."); // resposta exclusiva do swagger, a partir do front-end é impossível cair nesse if.
        }
        try {
            usuario.setEnabled(false);
            usuarioService.save(usuario);
            return ResponseEntity.ok().body("Usuário inativado com sucesso");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao inativar usuário.");
        }
    }
}
