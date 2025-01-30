function modalEditarEstoque(button) {
    var idEstoqueEdicao = button.getAttribute('data-id')
    $('#modalEditarEstoque').modal('show')
    document.getElementById('idEstoqueEdicao').value = idEstoqueEdicao;
}

function confirmarExclusaoEstoque(button) {
    var idEstoqueExclusao = button.getAttribute('data-id')
    Swal.fire({
        title: 'Tem certeza que deseja excluir o estoque desse produto?',
        text: "Essa ação não pode ser desfeita!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sim, excluir.',
        cancelButtonText: 'Não, voltar.',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            excluirEstoque(idEstoqueExclusao);
        } else {
            Swal.fire('Cancelado', 'O estoque não foi excluído', 'info');
        }
    });
}

function salvarEdicaoEstoque() {
    var idEstoqueEdicao = document.getElementById('idEstoqueEdicao').value;
    var produtoEstoqueEdicao = document.getElementById('produtoEstoqueEdicao').value;
    var estoqueMinimoEdicao = document.getElementById('estoqueMinimoEdicao').value;
    var estoqueAtualEdicao = document.getElementById('estoqueAtualEdicao').value;

    $.ajax({
        url: '/estoque/editarEstoque',
        type: 'POST',
        data: {
            idEstoqueEdicao: idEstoqueEdicao,
            produtoEstoqueEdicao: produtoEstoqueEdicao,
            estoqueMinimoEdicao: estoqueMinimoEdicao,
            estoqueAtualEdicao: estoqueAtualEdicao
        },
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    $('#modalEditarEstoque').modal('hide')
                    Swal.fire({
                        title: "Pronto!",
                        text: "O estoque do produto foi editado com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/estoque/listarEstoque";
                        }
                    });
                    break;
                case 500:
                    $('#modalEditarEstoque').modal('hide')
                    Swal.fire({
                        title: "Erro!",
                        text: "Ocorreu um erro ao editar o estoque.",
                        icon: "error"
                    });
                    break;
                default:
                    alert("Erro desconhecido: " + status);
            }
        }
    });
}

function excluirEstoque(idEstoqueExclusaoButton) {

    $.ajax({
        url: '/estoque/excluirEstoque',
        type: 'POST',
        data: {
            idEstoqueExclusao: idEstoqueExclusaoButton
        },
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "O estoque foi excluído com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/estoque/listarEstoque";
                        }
                    });
                    break;
                case 500:
                    Swal.fire({
                        title: "Erro!",
                        text: "Ocorreu um erro ao excluir este estoque.",
                        icon: "error"
                    });
                    break;

                default:
                    alert("Erro desconhecido: " + status);
            }
        }
    });

}