package com.ogl.MarketInfo.service;

import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.repository.ProdutosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutosService {
    @Autowired
    private ProdutosRepository produtosRepository;

    public Produtos salvar(Produtos produto) {
        return produtosRepository.save(produto);
    }

    public List<Produtos> listarTodos() {
        return produtosRepository.findAll();
    }

    public Produtos buscarPorId(Long id) {
        return produtosRepository.findById(id);
    }

    public void excluir(Long id) {
        produtosRepository.deleteById(Math.toIntExact(id));
    }
}
