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
    const precoAtualField = document.getElementById('precoAtual');
    precoAtualField.value = precoAtualField.value.replace('R$', '').replace(/\./g, '').replace(',', '.');
});

function modalEditarPreco(button) {
    var idPrecoEdicao = button.getAttribute('data-id');
    $('#modalEditarPreco').modal('show');
    document.getElementById('idPrecoEdicao').value = idPrecoEdicao;

    applyCleave('#precoAtual');
}

function modalExcluirPreco(button) {
    var idPrecoExclusao = button.getAttribute('data-id');
    $('#modalExcluirPreco').modal('show');
    document.getElementById('idPrecoExclusao').value = idPrecoExclusao;
}
