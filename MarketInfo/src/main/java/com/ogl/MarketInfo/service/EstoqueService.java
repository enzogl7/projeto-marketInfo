package com.ogl.MarketInfo.service;

import com.ogl.MarketInfo.model.Estoque;
import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Estoque> findById(Long id) {
        return estoqueRepository.findById(id);
    }

    public void excluirPorId(Long id) {
        estoqueRepository.deleteById(id);
    }

    public boolean existeEstoqueParaEsseProduto(Produtos produtos) {
        Optional<Estoque> estoque = estoqueRepository.findByProdutoId(produtos.getId());
        if(estoque.isPresent()) {
            return true;
        }
        return false;
    }

    public boolean isEstoqueCritico(Estoque estoque) {

        if (estoque.getQtdeEstoqueAtual() <= estoque.getQtdeEstoqueMinimo() + 10) {
            return true;
        }
        return false;
    }
}
