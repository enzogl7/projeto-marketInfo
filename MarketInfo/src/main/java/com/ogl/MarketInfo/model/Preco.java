package com.ogl.MarketInfo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Date;

@Table(name = "preco")
@Entity(name = "Preco")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Preco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produtos produto;

    @Column
    private double preco;

    @Column(nullable = true)
    private double precoAtual;

    @Column
    private LocalDate dataInicioVigor;

    @Column
    private LocalDate dataFimVigor;

    @Column(nullable = true)
    private String motivoAlteracao;

    private LocalDate dataAlteracao;

    // adicionar questao do usuario responsavel futuramente

}
