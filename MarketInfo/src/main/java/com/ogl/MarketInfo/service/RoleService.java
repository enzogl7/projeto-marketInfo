package com.ogl.MarketInfo.service;

import com.ogl.MarketInfo.model.Role;
import com.ogl.MarketInfo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role findById(Long id) {
        return roleRepository.findById(id).get();
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
