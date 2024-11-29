package com.ogl.MarketInfo.service;

import com.ogl.MarketInfo.model.Estoque;
import com.ogl.MarketInfo.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {
    @Autowired
    private EstoqueRepository estoqueRepository;

    public Estoque salvar(Estoque estoque) {
        return estoqueRepository.save(estoque);
    }

    public List<Estoque> findAll(){
        return estoqueRepository.findAll();
    }

    public Estoque findById(Long id) {
        return estoqueRepository.findById(id).orElse(null);
    }

    public void excluirPorId(Long id) {
        estoqueRepository.deleteById(id);
    }
}
