package com.ogl.MarketInfo.repository;

import com.ogl.MarketInfo.dtos.RolesDTO;
import com.ogl.MarketInfo.dtos.UsuarioDto;
import com.ogl.MarketInfo.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("FindByUsername success")
    void findByUsernameSuccess() {
        String username = "teste";
        List<RolesDTO> role = Collections.singletonList(new RolesDTO(1L, "ROLE_USER"));
        UsuarioDto data = new UsuarioDto(1L, username , "teste@teste", "senhateste", true, role);
        this.createUsuario(data);
        Optional<Usuario> resultado = this.usuarioRepository.findByUsername(username);
        assertThat(resultado.isPresent()).isTrue();
    }

    @Test
    @DisplayName("FindByUsername error (user not exists)")
    void findByUsernameError() {
        String username = "teste";
        Optional<Usuario> resultado = this.usuarioRepository.findByUsername(username);
        assertThat(resultado.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("findAllComRoles success")
    @Transactional
    void findAllComRolesSuccess() {
        // query para inserir dados nas tabela roles usada no join dessa consulta
        em.createNativeQuery("INSERT INTO roles (id, role_name) VALUES (1, 'ROLE_USER')").executeUpdate();
        em.createNativeQuery("INSERT INTO roles (id, role_name) VALUES (2, 'ROLE_ADMIN')").executeUpdate();

        List<RolesDTO> roles1 = Collections.singletonList(new RolesDTO(1L, "ROLE_USER"));
        List<RolesDTO> roles2 = Collections.singletonList(new RolesDTO(2L, "ROLE_ADMIN"));

        UsuarioDto data1 = new UsuarioDto(null, "teste", "teste@teste", "senhateste", true, roles1);
        UsuarioDto data2 = new UsuarioDto(null, "teste2", "teste2@teste2", "senhateste2", true, roles2);

        Usuario usuario1 = this.createUsuario(data1);
        Usuario usuario2 = this.createUsuario(data2);

        em.flush();

        // query para inserir dados nas tabela usuario_roles usada no join dessa consulta
        em.createNativeQuery("INSERT INTO usuario_roles (usuario_id, role_id) VALUES (:usuarioId1, 1)")
                .setParameter("usuarioId1", usuario1.getId())
                .executeUpdate();

        em.createNativeQuery("INSERT INTO usuario_roles (usuario_id, role_id) VALUES (:usuarioId2, 2)")
                .setParameter("usuarioId2", usuario2.getId())
                .executeUpdate();

        em.flush();
        em.clear();

        List<Usuario> resultado = usuarioRepository.findAllComRoles();
        assertThat(!resultado.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("findAllComRoles error (nao existe usuario com roles)")
    @Transactional
    void findAllComRolesError() {
        List<Usuario> resultado = usuarioRepository.findAllComRoles();
        assertThat(resultado.isEmpty()).isTrue();
    }



    // Métod0 utilizado para criar usuário dentro dos casos de teste
    private Usuario createUsuario(UsuarioDto data) {
        Usuario newUsuario = new Usuario(data);
        this.em.persist(newUsuario);
        return newUsuario;
    }
}