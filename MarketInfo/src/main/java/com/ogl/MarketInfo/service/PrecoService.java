package com.ogl.MarketInfo.service;

import com.ogl.MarketInfo.model.Preco;
import com.ogl.MarketInfo.repository.PrecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrecoService {
    @Autowired
    private PrecoRepository precoRepository;

    public List<Preco> listarTodos(){
        return precoRepository.findAll();
    }

    public Preco salvar(Preco p){
        return precoRepository.save(p);
    }
}
