package com.ogl.MarketInfo.repository;

import com.ogl.MarketInfo.model.Preco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrecoRepository extends JpaRepository<Preco, Long> {
    Optional<Preco> findByProdutoId(Long id);
}
