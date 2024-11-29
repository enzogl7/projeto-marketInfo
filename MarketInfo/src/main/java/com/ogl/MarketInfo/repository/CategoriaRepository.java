package com.ogl.MarketInfo.repository;

import com.ogl.MarketInfo.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
