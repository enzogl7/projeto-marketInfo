package com.ogl.MarketInfo.repository;

import com.ogl.MarketInfo.model.Produtos;
import jakarta.persistence.EntityManager;
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
class ProdutosRepositoryTest {

    @Autowired
    ProdutosRepository produtoRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("findProdutosByUsuario success (encontra produtos por id do usuario) // (find products by user id)")
    void findProdutosByUsuarioSuccess() {
        em.createNativeQuery("INSERT INTO categoria (id, nome, status) VALUES (1, 'Categoria 1', true)").executeUpdate();
        em.createNativeQuery("INSERT INTO usuarios (id, enabled, password, username, email) VALUES (1, true, 'senha', 'teste', 'teste@teste.com')").executeUpdate();
        em.createNativeQuery("INSERT INTO produtos (id, marca, nome, categoria, usuario) VALUES (1, 'marca', 'nome', 1, 1)").executeUpdate();

        List<Produtos> resultado = this.produtoRepository.findProdutosByUsuario(1L);
        assertThat(!resultado.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("findProdutosByUsuarioError (nao encotra produtos por usuario) // (doesnt find products for this user id)")
    void findProdutosByUsuarioError() {
        List<Produtos> resultado = this.produtoRepository.findProdutosByUsuario(1L);
        assertThat(resultado.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("findProdutosByCategoriaSuccess (encontra produtos pelo id da categoria) // (find products for this category id)")
    void findProdutosByCategoriaSuccess() {
        em.createNativeQuery("INSERT INTO categoria (id, nome, status) VALUES (1, 'Categoria 1', true)").executeUpdate();
        em.createNativeQuery("INSERT INTO produtos (id, marca, nome, categoria) VALUES (1, 'marca', 'nome', 1)").executeUpdate();

        List<Produtos> resultado = this.produtoRepository.findProdutosByCategoria(1L);
        assertThat(!resultado.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("findProdutosByCategoriaError (não encontra produtos pelo id da categoria) // (doesnt find products for this category id)")
    void findProdutosByCategoriaError() {
        List<Produtos> resultado = this.produtoRepository.findProdutosByCategoria(1L);
        assertThat(resultado.isEmpty()).isTrue();
    }

}