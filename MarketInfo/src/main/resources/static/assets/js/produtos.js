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
        console.log(usuario)

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

function modalExcluirProduto(button) {
    var idProdutoExclusao = button.getAttribute('data-id')
    $('#modalExcluirProduto').modal('show')
    document.getElementById('idProdutoExclusao').value = idProdutoExclusao;
}