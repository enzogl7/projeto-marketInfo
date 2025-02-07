package com.ogl.MarketInfo.service;

import com.ogl.MarketInfo.model.Role;
import com.ogl.MarketInfo.model.Usuario;
import com.ogl.MarketInfo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String username = authentication.getName();
            return usuarioRepository.findByUsername(username).orElse(null);
        }
        return null;
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> findAllComRoles() {
        return usuarioRepository.findAllComRoles();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).get();
    }

    public void save(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public void associarRoleAoUsuario(Long userId, Long roleId) {
        String sql = "INSERT INTO usuario_roles (usuario_id, role_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        jdbcTemplate.update(sql, userId, roleId);
    }

    public List<String> findAllRolesUsuarios() {
        List<String> rolesTabela = new ArrayList<>();

        List<Usuario> usuarioList = usuarioRepository.findAll();

        for (Usuario usuario : usuarioList) {
            for (Role role : usuario.getRoles()) {
                rolesTabela.add(role.getRoleName());
            }
        }

        return rolesTabela;
    }

    public void removerRoleDoUsuario(Long usuarioId, Long roleId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.getRoles().removeIf(role -> role.getId().equals(roleId));

        usuarioRepository.save(usuario);
    }

    public void removerRolesDoUsuario(Long usuarioId) {
        String deleteSql = "DELETE FROM usuario_roles WHERE usuario_id = ?";
        jdbcTemplate.update(deleteSql, usuarioId);
    }

    public void excluirUsuario(Long usuarioId) {
        usuarioRepository.deleteById(usuarioId);
    }

    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username).get();
    }

    public List<String> getRolesUsuarioLogado() {
        List<String> nomeRolesUsuario = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Usuario usuarioLogado = findByUsername(username);
        List<Role> rolesUsuarioLogado = usuarioLogado.getRoles();
        for (Role role : rolesUsuarioLogado) {
            nomeRolesUsuario.add(role.getRoleName());
        }
        return nomeRolesUsuario;
    }
}
