package com.ogl.MarketInfo.model;

import jakarta.persistence.*;

@Table(name = "preco")
@Entity(name = "Preco")
public class Preco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produtos produto;

    @Column
    private double preco;
}
