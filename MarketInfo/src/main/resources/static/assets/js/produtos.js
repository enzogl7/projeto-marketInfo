function modalEditarProduto(button) {
    var idProdutoEdicao = button.getAttribute('data-id')
    $('#modalEditarProduto').modal('show')
    document.getElementById('idProdutoEdicao').value = idProdutoEdicao;
}

function modalExcluirProduto(button) {
    var idProdutoExclusao = button.getAttribute('data-id')
    $('#modalExcluirProduto').modal('show')
    document.getElementById('idProdutoExclusao').value = idProdutoExclusao;
}