package com.ogl.MarketInfo.repository;

import com.ogl.MarketInfo.model.Preco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrecoRepository extends JpaRepository<Preco, Long> {
}
