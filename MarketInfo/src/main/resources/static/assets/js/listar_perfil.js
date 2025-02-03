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

function confirmacaoExclusaoPerfil(button) {
    var idPerfilExclusao = button.getAttribute('data-id')
    Swal.fire({
        title: 'Tem certeza que deseja excluir este perfil?',
        text: "Essa ação não pode ser desfeita!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sim, excluir este perfil.',
        cancelButtonText: 'Não, voltar.',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            excluirPerfil(idPerfilExclusao);
        } else {
            Swal.fire('Cancelado', 'O perfil não foi excluído', 'info');
        }
    });
}

function excluirPerfil(idButton) {
    $.ajax({
        url: '/perfil/excluirperfil',
        type: 'POST',
        data: {
            idPerfilExclusao: idButton
        },
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "O perfil foi excluído com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/perfil/listarperfil";
                        }
                    });
                    break;
                case 304:
                    Swal.fire({
                        title: "Ops!",
                        text: "Não foi possível excluir este perfil pois ele está atribuído à algum usuário.",
                        icon: "warning",
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/perfil/listarperfil";
                        }
                    });
                    break;
                case 500:
                    Swal.fire({
                        title: "Erro!",
                        text: "Ocorreu um erro ao excluir este perfil.",
                        icon: "error"
                    });
                    break;
                default:
                    alert("Erro desconhecido: " + status);
            }
        }
    });
}