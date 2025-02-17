package com.ogl.MarketInfo.repository;

import com.ogl.MarketInfo.model.OpcoesMensageria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OpcoesMensageriaRepository extends JpaRepository<OpcoesMensageria, Long> {
    Optional<OpcoesMensageria> findById(Long id);
}
