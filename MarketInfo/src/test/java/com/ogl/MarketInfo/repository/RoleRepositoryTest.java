package com.ogl.MarketInfo.repository;

import com.ogl.MarketInfo.dtos.RolesDTO;
import com.ogl.MarketInfo.model.Role;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("findUsuariosVinculados success (encontra usuários com role)")
    @Transactional
    void findUsuariosVinculadosSuccess() {
        em.createNativeQuery("INSERT INTO roles (id, role_name) VALUES (1, 'ROLE_USER')").executeUpdate();

        // insere um usuário e associa a role a ele
        em.createNativeQuery("INSERT INTO usuarios (id, enabled, password, username, email) VALUES (1, true, 'senha', 'teste', 'teste@teste.com')").executeUpdate();
        em.createNativeQuery("INSERT INTO usuario_roles (usuario_id, role_id) VALUES (1, 1)").executeUpdate();

        em.flush();
        em.clear();

        List<Long> resultado = this.roleRepository.findUsuariosVinculados(1L);
        assertThat(!resultado.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("findUsuariosVinculados error (não encontra usuários com role)")
    @Transactional
    void findUsuariosVinculadosError() {
        List<Long> resultado = this.roleRepository.findUsuariosVinculados(1L);
        assertThat(resultado.isEmpty()).isTrue();
    }

}