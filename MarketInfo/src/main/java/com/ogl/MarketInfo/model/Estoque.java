package com.ogl.MarketInfo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "estoque")
@Entity(name = "Estoque")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

}
