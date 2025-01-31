package com.ogl.MarketInfo.repository;

import com.ogl.MarketInfo.model.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutosRepository extends JpaRepository<Produtos, Integer> {
    Produtos findById(Long id);


    @Query(value = "select * from Produtos where usuario = :idUsuario", nativeQuery = true)
    List<Produtos> findProdutosByUsuario(@Param("idUsuario") Long idUsuario);
}
