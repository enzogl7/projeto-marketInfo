package com.ogl.MarketInfo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "opcoes_mensageria")
@Entity(name = "opcoes_mensageria")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpcoesMensageria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private List<String> emails;

    private boolean alertasEstoques;
    private boolean estoqueCadastro;
    private boolean estoqueEdicao;
    private boolean estoqueMinimo;

    private boolean alertasProdutos;
    private boolean produtosCadastro;
    private boolean produtosEdicao;
}
