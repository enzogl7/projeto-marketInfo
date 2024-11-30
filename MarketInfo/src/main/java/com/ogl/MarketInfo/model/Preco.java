package com.ogl.MarketInfo.model;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Date;

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

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(double precoAtual) {
        this.precoAtual = precoAtual;
    }

    public LocalDate getDataInicioVigor() {
        return dataInicioVigor;
    }

    public void setDataInicioVigor(LocalDate dataInicioVigor) {
        this.dataInicioVigor = dataInicioVigor;
    }

    public LocalDate getDataFimVigor() {
        return dataFimVigor;
    }

    public void setDataFimVigor(LocalDate dataFimVigor) {
        this.dataFimVigor = dataFimVigor;
    }

    public String getMotivoAlteracao() {
        return motivoAlteracao;
    }

    public void setMotivoAlteracao(String motivoAlteracao) {
        this.motivoAlteracao = motivoAlteracao;
    }

    public String getDiferencaFormatada() {
        if (precoAtual > 0) {
            double diferenca = precoAtual - preco;
            DecimalFormat df = new DecimalFormat("#,##0.00");
            return df.format(diferenca);
        }
        return null;
    }

    public String getPrecoAtualFormatado() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return "R$ " + df.format(precoAtual);
    }

    public String getPrecoFormatado() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return "R$ " + df.format(preco);
    }

    public LocalDate getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDate dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }
}
