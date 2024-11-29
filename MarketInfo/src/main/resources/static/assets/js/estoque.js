function modalEditarEstoque(button) {
    var idEstoqueEdicao = button.getAttribute('data-id')
    $('#modalEditarEstoque').modal('show')
    document.getElementById('idEstoqueEdicao').value = idEstoqueEdicao;
}

function modalExcluirEstoque(button) {
    var idEstoqueExclusao = button.getAttribute('data-id')
    $('#modalExcluirEstoque').modal('show')
    document.getElementById('idEstoqueExclusao').value = idEstoqueExclusao;
}