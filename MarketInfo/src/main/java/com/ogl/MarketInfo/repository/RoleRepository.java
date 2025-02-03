package com.ogl.MarketInfo.repository;

import com.ogl.MarketInfo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleUser);

    @Query(value = "select ur.role_id from usuario_roles ur where role_id = :id", nativeQuery = true)
    List<Long> findUsuariosVinculados(@Param("id") Long id);
}
