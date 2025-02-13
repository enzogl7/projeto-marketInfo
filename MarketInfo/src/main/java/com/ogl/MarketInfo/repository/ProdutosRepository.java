package com.ogl.MarketInfo.repository;

import com.ogl.MarketInfo.model.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdutosRepository extends JpaRepository<Produtos, Integer> {
    Optional<Produtos> findById(Long id);

    @Query(value = "select * from Produtos where usuario = :idUsuario", nativeQuery = true)
    List<Produtos> findProdutosByUsuario(@Param("idUsuario") Long idUsuario);

    @Query(value = "select * from produtos where categoria = :idCategoria", nativeQuery = true)
    List<Produtos> findProdutosByCategoria(@Param("idCategoria") Long idCategoria);
}
