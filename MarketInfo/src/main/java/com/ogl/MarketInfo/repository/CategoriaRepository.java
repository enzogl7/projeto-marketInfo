package com.ogl.MarketInfo.repository;

import com.ogl.MarketInfo.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findById(Long id);

    @Query(value = "select * from categoria where status = true", nativeQuery = true)
    List<Categoria> findAllWhereStatusTrue();
}
