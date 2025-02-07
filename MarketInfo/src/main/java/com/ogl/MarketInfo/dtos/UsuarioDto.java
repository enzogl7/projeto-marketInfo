package com.ogl.MarketInfo.dtos;

import com.ogl.MarketInfo.model.Role;

import java.util.List;

public record UsuarioDto(Long id, String username, String email, String password, boolean enabled, List<RolesDTO> roles) {
}
