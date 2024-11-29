package com.ogl.MarketInfo.model;

import jakarta.persistence.*;

import java.util.List;

@Table(name = "categoria")
@Entity(name = "Categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @OneToMany(mappedBy = "categoria")
    private List<Produtos> produtos;
}
