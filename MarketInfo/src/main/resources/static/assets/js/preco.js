document.addEventListener('DOMContentLoaded', function () {
    applyCleave('#preco');
    applyCleave('#precoAtual');
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
