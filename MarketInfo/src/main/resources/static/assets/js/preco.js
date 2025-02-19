// FORMATAÇÃO PREÇO E FUNÇÃO CHECKBOX
document.addEventListener('DOMContentLoaded', function () {
    configureCheckbox("semDataFinal", "dataFinal", "dataFinalContainer");
    configureCheckbox("semDataFinalEdicao", "dataFinalEdicao", "dataFinalEdicaoContainer");
});

function limparPrecoParaEnvio(valor) {
    if (!valor) return '';
    valor = valor.replace('R$', '').replace(/\./g, '').replace(',', '.').trim();
    return valor;
}

document.getElementById('formPreco').addEventListener('submit', function(event) {
    var precoInput = document.getElementById('preco');
    precoInput.value = limparPrecoParaEnvio(precoInput.value);
});

document.getElementById('editarPrecoForm').addEventListener('submit', function(event) {
    var precoInputEdicao = document.getElementById('precoAtual');
    precoInputEdicao.value = limparPrecoParaEnvio(precoInputEdicao.value);
});

document.getElementById('pesquisaPreco').addEventListener('submit', function(event) {
    var precoInputPesquisa = document.getElementById('pesquisaPreco');
    precoInputPesquisa.value = limparPrecoParaEnvio(precoInputPesquisa.value);
});

function formatarPreco(input) {
    let valor = input.value;

    valor = valor.replace(/\D/g, '');

    if (valor.length > 2) {
        valor = valor.replace(/(\d)(\d{2})$/, '$1,$2');
    }

    valor = valor.replace(/(\d)(?=(\d{3})+(\,|$))/g, '$1.');

    input.value = valor ? 'R$ ' + valor : '';

}


function configureCheckbox(checkboxId, inputId, containerId) {
    const checkboxElement = document.getElementById(checkboxId);
    const inputElement = document.getElementById(inputId);
    const containerElement = document.getElementById(containerId);

    if (checkboxElement) {
        checkboxElement.addEventListener("change", function () {
            if (this.checked) {
                containerElement.style.display = "block";
                inputElement.setAttribute("required", "true");
            } else {
                containerElement.style.display = "none";
                inputElement.removeAttribute("required");
            }
        });
    }
}
// FIM FORMATAÇÃO PREÇO E FUNÇÃO CHECKBOX


// INICIO ORDENACAO TABELA
let ordemAscendente = true;

function ordenarTabela(colunaIndex, tipo) {
    const tabela = document.getElementById("tabelaPrecos");
    const linhas = Array.from(tabela.querySelectorAll("tr"));

    ordemAscendente = !ordemAscendente;

    linhas.sort((a, b) => {
        const valorA = a.children[colunaIndex]?.textContent.trim();
        const valorB = b.children[colunaIndex]?.textContent.trim();

        if (tipo === "numero") {
            const numeroA = parseFloat(valorA.replace("R$", "").replace(",", "."));
            const numeroB = parseFloat(valorB.replace("R$", "").replace(",", "."));
            return ordemAscendente ? numeroA - numeroB : numeroB - numeroA;
        }

        if (tipo === "data") {
            const dataA = new Date(valorA.split("/").reverse().join("-"));
            const dataB = new Date(valorB.split("/").reverse().join("-"));
            return ordemAscendente ? dataA - dataB : dataB - dataA;
        }

        return 0;
    });

    linhas.forEach((linha) => tabela.appendChild(linha));
}
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

// INICIO MODAIS
function modalEditarPreco(button) {
    var idPrecoEdicao = button.getAttribute('data-id');
    const produtoId = button.getAttribute('data-produto-id');

    $('#modalEditarPreco').modal('show');
    document.getElementById('idPrecoEdicao').value = idPrecoEdicao;
    document.getElementById("precoAtual").value = "";

    const produtoSelect = document.getElementById('produtoPrecoEdicao');
    if (produtoSelect) {
        produtoSelect.value = produtoId;
        produtoSelect.disabled = true;
    }

    const precoAtualField = document.getElementById('precoAtual');
    if (precoAtualField && precoAtualField.value) {
        precoAtualField.value = 'R$ ' + precoAtualField.value;
    }
}
//FIM MODAIS

function confirmacaoExcluirPreco(button) {
    var idPrecoExclusao = button.getAttribute('data-id');

    Swal.fire({
        title: 'Tem certeza que deseja excluir a precificação desse produto?',
        text: "Essa ação não pode ser desfeita!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sim, excluir.',
        cancelButtonText: 'Não, voltar.',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            excluirPreco(idPrecoExclusao);
        } else {
            Swal.fire('Cancelado', 'A precificação não foi excluída', 'info');
        }
    });
}

function excluirPreco(idPrecoExclusaoButton) {
    $.ajax({
        url: '/preco/excluirPreco',
        type: 'POST',
        data: {
            idPrecoExclusao: idPrecoExclusaoButton
        },
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "O preço foi excluído com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/preco/listarPrecos";
                        }
                    });
                    break;
                case 500:
                    Swal.fire({
                        title: "Erro!",
                        text: "Ocorreu um erro ao excluir este preço.",
                        icon: "error"
                    });
                    break;

                default:
                    alert("Erro desconhecido: " + status);
            }
        }
    });
}

function salvarEdicaoPreco() {
    var idPrecoEdicao = document.getElementById('idPrecoEdicao').value;
    var produtoPrecoEdicao = document.getElementById('produtoPrecoEdicao').value;
    var precoAtual = document.getElementById('precoAtual').value;
    var dataInicioEdicao = document.getElementById('dataInicioEdicao').value;
    var dataFinalEdicao = document.getElementById('dataFinalEdicao').value;
    var motivoAlteracaoPreco = document.getElementById('motivoAlteracaoPreco').value;

    $.ajax({
        url: '/preco/editarPreco',
        type: 'POST',
        data: {
            idPrecoEdicao: idPrecoEdicao,
            produtoPrecoEdicao: produtoPrecoEdicao,
            precoAtual: precoAtual,
            dataInicioEdicao: dataInicioEdicao,
            dataFinalEdicao: dataFinalEdicao,
            motivoAlteracaoPreco: motivoAlteracaoPreco
        },
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    $('#modalEditarPreco').modal('hide')
                    Swal.fire({
                        title: "Pronto!",
                        text: "O preço foi editado com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/preco/listarPrecos";
                        }
                    });
                    break;
                case 500:
                    $('#modalEditarPreco').modal('hide')
                    Swal.fire({
                        title: "Erro!",
                        text: "Ocorreu um erro ao editar este preço.",
                        icon: "error"
                    });
                    break;
                default:
                    alert("Erro desconhecido: " + status);
            }
        }
    });
}
