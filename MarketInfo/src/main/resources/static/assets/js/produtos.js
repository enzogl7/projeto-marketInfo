document.getElementById("pesquisaNome").addEventListener("keyup", filtrarTabela);
document.getElementById("pesquisaMarca").addEventListener("keyup", filtrarTabela);
document.getElementById("pesquisaUsuario").addEventListener("change", filtrarTabela);

function filtrarTabela() {
    var nomeFilter = document.getElementById("pesquisaNome").value.toLowerCase();
    var marcaFilter = document.getElementById("pesquisaMarca").value.toLowerCase();
    var usuarioFilter = document.getElementById("pesquisaUsuario").value.toLowerCase();

    var table = document.querySelector(".table");
    var rows = table.querySelectorAll("tbody tr");

    rows.forEach(function(row) {
        var nome = row.querySelector("td:nth-child(1)").textContent.toLowerCase();
        var marca = row.querySelector("td:nth-child(2)").textContent.toLowerCase();
        var usuario = row.querySelector("td:nth-child(6)").textContent.toLowerCase();

        var nomeMatch = nome.indexOf(nomeFilter) > -1 || nomeFilter === "";
        var marcaMatch = marca.indexOf(marcaFilter) > -1 || marcaFilter === "";
        var usuarioMatch = usuario.indexOf(usuarioFilter) > -1 || usuarioFilter === "";


        if (nomeMatch && marcaMatch && usuarioMatch) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}


function modalEditarProduto(button) {
    var idProdutoEdicao = button.getAttribute('data-id')
    $('#modalEditarProduto').modal('show')
    document.getElementById('idProdutoEdicao').value = idProdutoEdicao;
}

function salvarEdicaoProduto() {
    var idProdutoEdicao = document.getElementById('idProdutoEdicao').value;
    var nomeProdutoEdicao = document.getElementById('nomeProdutoEdicao').value;
    var categoriaEdicao = document.getElementById('categoriaEdicao').value;
    var marcaEdicao = document.getElementById('marcaEdicao').value;

    $.ajax({
        url: '/produtos/editarProduto',
        type: 'POST',
        data: {
            idProdutoEdicao: idProdutoEdicao,
            nomeProdutoEdicao: nomeProdutoEdicao,
            categoriaEdicao: categoriaEdicao,
            marcaEdicao: marcaEdicao
        },
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    $('#modalEditarProduto').modal('hide')
                    Swal.fire({
                        title: "Pronto!",
                        text: "O produto foi editado com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/produtos/listarProdutos";
                        }
                    });
                    break;
                case 500:
                    $('#modalEditarProduto').modal('hide')
                    Swal.fire({
                        title: "Erro!",
                        text: "Ocorreu um erro ao editar este produto.",
                        icon: "error"
                    });
                    break;
                default:
                    alert("Erro desconhecido: " + status);
            }
        }
    });
}

function confirmarExclusaoProduto(button) {
    var idProdutoExclusao = button.getAttribute('data-id')
    Swal.fire({
        title: 'Tem certeza que deseja excluir este produto?',
        text: "Essa ação não pode ser desfeita!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sim, excluir este produto.',
        cancelButtonText: 'Não, voltar.',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            excluirProduto(idProdutoExclusao);
        } else {
            Swal.fire('Cancelado', 'O produto não foi excluído', 'info');
        }
    });
}

function excluirProduto(idButton) {
    $.ajax({
        url: '/produtos/excluirProduto',
        type: 'POST',
        data: {
            idProdutoExclusao: idButton
        },
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "O produto foi excluído com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/produtos/listarProdutos";
                        }
                    });
                    break;
                case 303:
                    Swal.fire({
                        title: "Ops!",
                        html: "Não foi possível excluir o produto selecionado pois está vinculado a algum registro na tabela de <b>preços</b>.",
                        icon: "warning",
                        confirmButtonText: 'OK'
                    });
                    break;
                case 304:
                    Swal.fire({
                        title: "Ops!",
                        html: "Não foi possível excluir o produto selecionado pois está vinculado a algum registro na tabela de <b>estoque</b>.",
                        icon: "warning",
                        confirmButtonText: 'OK'
                    });
                    break;
                case 500:
                    Swal.fire({
                        title: "Erro!",
                        text: "Ocorreu um erro ao excluir este produto.",
                        icon: "error"
                    });
                    break;

                default:
                    alert("Erro desconhecido: " + status);
            }
        }
    });

}