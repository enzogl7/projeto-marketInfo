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

    @Column(name = "qtde_estoque_minimo")
    private Integer qtdeEstoqueMinimo;

    @Column(name = "qtde_estoque_atual")
    private Integer qtdeEstoqueAtual;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produtos getProduto() {
        return produto;
    }

    public void setProduto(Produtos produto) {
        this.produto = produto;
    }

    public Integer getQtdeEstoqueMinimo() {
        return qtdeEstoqueMinimo;
    }

    public void setQtdeEstoqueMinimo(Integer qtdeEstoqueMinimo) {
        this.qtdeEstoqueMinimo = qtdeEstoqueMinimo;
    }

    public Integer getQtdeEstoqueAtual() {
        return qtdeEstoqueAtual;
    }

    public void setQtdeEstoqueAtual(Integer qtdeEstoqueAtual) {
        this.qtdeEstoqueAtual = qtdeEstoqueAtual;
    }
}
