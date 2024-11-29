package com.ogl.MarketInfo.repository;

import com.ogl.MarketInfo.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
}
