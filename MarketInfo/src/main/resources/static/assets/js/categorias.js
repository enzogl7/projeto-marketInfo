document.getElementById("pesquisaNomeCategoria").addEventListener("keyup", filtrarTabela);
document.getElementById("pesquisaDescricaoCategoria").addEventListener("keyup", filtrarTabela);
document.getElementById("pesquisaDataCriacaoCategoria").addEventListener("change", filtrarTabela);

function filtrarTabela() {
    var nomeFilter = document.getElementById("pesquisaNomeCategoria").value.toLowerCase();
    var descricaoFilter = document.getElementById("pesquisaDescricaoCategoria").value.toLowerCase();
    var dataFilter = document.getElementById("pesquisaDataCriacaoCategoria").value.toLowerCase();

    var table = document.querySelector(".table");
    var rows = table.querySelectorAll("tbody tr");

    rows.forEach(function(row) {
        var nome = row.querySelector("td:nth-child(1)").textContent.toLowerCase();
        var descricao = row.querySelector("td:nth-child(2)").textContent.toLowerCase();
        var dataCriacao = row.querySelector("td:nth-child(3)").textContent.toLowerCase();

        var nomeMatch = nome.indexOf(nomeFilter) > -1 || nomeFilter === "";
        var descricaoMatch = descricao.indexOf(descricaoFilter) > -1 || descricaoFilter === "";
        var dataMatch = dataCriacao.indexOf(dataFilter) > -1 || dataFilter === "";


        if (nomeMatch && descricaoMatch && dataMatch) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}

function modalEditarCategoria(button) {
    var idCategoriaEdicao = button.getAttribute('data-id')
    var nomeCategoriaEdicao = button.getAttribute('data-nome')
    var descricaoCategoriaEdicao = button.getAttribute('data-descricao')

    $('#modalEditarCategoria').modal('show')
    document.getElementById('idCategoriaEdicao').value = idCategoriaEdicao;
    document.getElementById('nomeCategoriaEdicao').value = nomeCategoriaEdicao;
    document.getElementById('descricaoCategoriaEdicao').value = descricaoCategoriaEdicao;

}

function salvarEdicaoCategoria() {
    var idCategoriaEdicao = document.getElementById('idCategoriaEdicao').value;
    var nomeCategoriaEdicao = document.getElementById('nomeCategoriaEdicao').value;
    var descricaoCategoriaEdicao = document.getElementById('descricaoCategoriaEdicao').value;
    var categoriaCheckboxEdicao = document.getElementById('categoriaCheckboxEdicao').checked;



    $.ajax({
        url: '/categoria/editarcategoria',
        type: 'POST',
        data: {
            idCategoriaEdicao: idCategoriaEdicao,
            nomeCategoriaEdicao: nomeCategoriaEdicao,
            descricaoCategoriaEdicao: descricaoCategoriaEdicao,
            categoriaCheckboxEdicao: categoriaCheckboxEdicao
        },
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    $('#modalEditarCategoria').modal('hide')
                    Swal.fire({
                        title: "Pronto!",
                        text: "A categoria foi editada com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/categoria/listarcategoria";
                        }
                    });
                    break;
                case 500:
                    $('#modalEditarCategoria').modal('hide')
                    Swal.fire({
                        title: "Erro!",
                        text: "Ocorreu um erro ao editar esta categoria.",
                        icon: "error"
                    });
                    break;
                default:
                    alert("Erro desconhecido: " + status);
            }
        }
    });
}
