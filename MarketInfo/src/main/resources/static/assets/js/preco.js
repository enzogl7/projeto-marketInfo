// INICIO ORDENACAO TABELA
// FIM ORDENACAO TABELA


// INICIO FILTRO/PESQUISA TABELA
document.getElementById("pesquisaNomePreco").addEventListener("keyup", filtrarTabelaPrecos);
document.getElementById("pesquisaPreco").addEventListener("keyup", filtrarTabelaPrecos);

function filtrarTabelaPrecos() {
    var nomeFilter = document.getElementById("pesquisaNomePreco").value.toLowerCase();
    var precoFilter = document.getElementById("pesquisaPreco").value.toLowerCase();
    var table = document.querySelector(".table");
    var rows = table.querySelectorAll("tbody tr");

    rows.forEach(function (row) {
        var nome = row.querySelector("td:nth-child(1)").textContent.toLowerCase();
        var marca = row.querySelector("td:nth-child(2)").textContent.toLowerCase();

        var nomeMatch = nome.indexOf(nomeFilter) > -1 || nomeFilter === "";
        var marcaMatch = marca.indexOf(precoFilter) > -1 || precoFilter === "";

        if (nomeMatch && marcaMatch) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}
// FIM FILTRO/PESQUISA TABELA

//FORMATAÇÃO DO INPUT DE PREÇO
document.addEventListener('DOMContentLoaded', function () {
    applyCleave('#preco');
    applyCleave('#precoAtual');
    applyCleave('#pesquisaPreco')
});

function applyCleave(inputSelector) {
    if (document.querySelector(inputSelector)) {
        new Cleave(inputSelector, {
            numeral: true,
            numeralThousandsGroupStyle: 'thousand',
            prefix: 'R$ ',
            noImmediatePrefix: true,
            decimalMark: ',',
            delimiters: ['.', ',']
        });
    }
}

document.getElementById('formPreco').addEventListener('submit', function(event) {
    var precoInput = document.getElementById('preco');
    var precoValor = precoInput.value;

    precoValor = precoValor.replace('R$', '').replace(/\D/g, '');
    precoInput.value = precoValor;
});

document.getElementById('editarPrecoForm').addEventListener('submit', function(event) {
    var precoInputEdicao = document.getElementById('precoAtual');
    var precoValorEdicao = precoInputEdicao.value;

    precoValorEdicao = precoValorEdicao.replace('R$', '').replace(/\D/g, '');
    precoInputEdicao.value = precoValorEdicao;
});

document.getElementById('pesquisaPreco').addEventListener('submit', function(event) {
    var precoInputPesquisa = document.getElementById('pesquisaPreco');
    var precoValorPesquisa = precoInputPesquisa.value;

    precoValorPesquisa = precoValorPesquisa.replace('R$', '').replace(/\D/g, '');
    precoInputPesquisa.value = precoValorPesquisa;
});
// FIM FORMATAÇÃO DE PREÇO

// INICIO MODAIS
function modalEditarPreco(button) {
    var idPrecoEdicao = button.getAttribute('data-id');
    $('#modalEditarPreco').modal('show');
    document.getElementById('idPrecoEdicao').value = idPrecoEdicao;

    applyCleave('#precoAtual');

    const precoAtualField = document.getElementById('precoAtual');
    if (precoAtualField && precoAtualField.value) {
        precoAtualField.value = 'R$ ' + precoAtualField.value;
    }
}

function modalExcluirPreco(button) {
    var idPrecoExclusao = button.getAttribute('data-id');
    $('#modalExcluirPreco').modal('show');
    document.getElementById('idPrecoExclusao').value = idPrecoExclusao;
}
//FIM MODAIS

