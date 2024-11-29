package com.ogl.MarketInfo.service;

import com.ogl.MarketInfo.model.Estoque;
import com.ogl.MarketInfo.model.Preco;
import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.repository.PrecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Preco buscaPorId(Long id){
        return precoRepository.findById(id).orElse(null);
    }

    public boolean existePrecoParaEsseProduto(Produtos produtos) {
        Optional<Preco> preco = precoRepository.findByProdutoId(produtos.getId());
        if(preco.isPresent()) {
            return true;
        }
        return false;
    }
}
