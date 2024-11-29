package com.ogl.MarketInfo.service;

import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.repository.ProdutosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutosService {
    @Autowired
    private ProdutosRepository produtosRepository;

    public Produtos salvar(Produtos produto) {
        return produtosRepository.save(produto);
    }
}
