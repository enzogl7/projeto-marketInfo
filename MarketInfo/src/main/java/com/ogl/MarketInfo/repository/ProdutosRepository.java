package com.ogl.MarketInfo.repository;

import com.ogl.MarketInfo.model.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutosRepository extends JpaRepository<Produtos, Integer> {
    Produtos findById(Long id);
}
