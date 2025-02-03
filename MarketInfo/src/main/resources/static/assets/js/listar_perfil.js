document.getElementById("pesquisaNomePerfil").addEventListener("input", filtrarTabela);

function filtrarTabela() {
    var nomeFilter = document.getElementById("pesquisaNomePerfil").value.toLowerCase();

    var table = document.querySelector(".table");
    var rows = table.querySelectorAll("tbody tr");

    rows.forEach(function(row) {
        var nome= row.querySelector("td:nth-child(1)").textContent.toLowerCase();
        var nomeMatch = nome.indexOf(nomeFilter) > -1 || nomeFilter === "";

        if (nomeMatch) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}