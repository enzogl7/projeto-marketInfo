package com.ogl.MarketInfo.repository;

import com.ogl.MarketInfo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    @Query(value = "select * from usuarios u where u.username = :username", nativeQuery = true)
    Usuario buscarPorUsername(@Param("username") String username);
}
