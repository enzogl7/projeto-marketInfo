package com.ogl.MarketInfo.service;

import com.ogl.MarketInfo.model.Categoria;
import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria salvar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Categoria findById(Long id) {
        return categoriaRepository.findById(id).get();
    }

    public List<Categoria> findAllAtivo() {
        List<Categoria> categoriasAtivas = categoriaRepository.findAllWhereStatusTrue();
        Collections.sort(categoriasAtivas, Comparator.comparing(Categoria::getNome));
        return categoriasAtivas;
    }

    public void deletar(Long idCategoria) {
        categoriaRepository.deleteById(idCategoria);
    }

}
