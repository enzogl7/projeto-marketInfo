package com.ogl.MarketInfo.service;

import com.ogl.MarketInfo.model.Role;
import com.ogl.MarketInfo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public void salvar(Role role) {
        roleRepository.save(role);
    }

    public void excluir(Long id) {
        roleRepository.deleteById(id);
    }

    public Boolean perfilPossuiVinculoAAlgumUsuario(Long id) {
        List<Long> usuariosVinculados = roleRepository.findUsuariosVinculados(id);
        if (usuariosVinculados.size() > 0) {
            return true;
        }
        return false;
    }
}
