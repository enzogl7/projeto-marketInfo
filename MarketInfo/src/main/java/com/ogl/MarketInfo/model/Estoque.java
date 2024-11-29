package com.ogl.MarketInfo.model;

import jakarta.persistence.*;

@Table(name = "estoque")
@Entity(name = "Estoque")
public class Estoque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produtos produto;

    @Column(name = "qtde_estoque")
    private Integer qtdeEstoque;
}
