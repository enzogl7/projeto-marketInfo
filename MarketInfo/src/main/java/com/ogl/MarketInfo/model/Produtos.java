package com.ogl.MarketInfo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "produtos")
@Entity(name = "Produtos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produtos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @ManyToOne
    @JoinColumn(name = "categoria", referencedColumnName = "id")
    private Categoria categoria;

    @Column
    private String marca;

    @Column
    private LocalDate dataCadastro;

    @Column(nullable = true)
    private LocalDate dataUltimaEdicao;

    @ManyToOne
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    private Usuario usuario;

}
