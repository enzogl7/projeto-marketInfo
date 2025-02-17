package com.ogl.MarketInfo.service;

import com.ogl.MarketInfo.model.OpcoesMensageria;
import com.ogl.MarketInfo.repository.OpcoesMensageriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OpcoesMensageriaService {
    @Autowired
    private OpcoesMensageriaRepository opcoesMensageriaRepository;

    public void salvar(OpcoesMensageria opcoesMensageria) {
        opcoesMensageriaRepository.save(opcoesMensageria);
    }

    public List<OpcoesMensageria> findAll(){
        return (opcoesMensageriaRepository.findAll());
    }
}
