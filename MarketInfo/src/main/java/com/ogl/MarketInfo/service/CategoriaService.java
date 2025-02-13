package com.ogl.MarketInfo.service;

import com.ogl.MarketInfo.model.Categoria;
import com.ogl.MarketInfo.model.Produtos;
import com.ogl.MarketInfo.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }

    public List<Categoria> findAllAtivo() {
        List<Categoria> categoriasAtivas = categoriaRepository.findByStatusTrue();
        Collections.sort(categoriasAtivas, Comparator.comparing(Categoria::getNome));
        return categoriasAtivas;
    }

    public void deletar(Long idCategoria) {
        categoriaRepository.deleteById(idCategoria);
    }

}
